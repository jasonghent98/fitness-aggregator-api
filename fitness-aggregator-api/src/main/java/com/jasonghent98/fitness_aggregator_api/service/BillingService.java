package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.config.BackendConfig;
import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.Subscription;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.SubscriptionRepository;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BillingService {

    private final StripeCustomerService stripeCustomerService;
    private final UserRepository userRepo;
    private final BackendConfig backendConfig;
    private final FrontendConfig frontendConfig;
    private final SubscriptionRepository subRepo;

    @Value("${stripe.price.enhanced.monthly}") private String enhancedMonthlyPrice;
    @Value("${stripe.price.enhanced.annual}")  private String enhancedAnnualPrice;

    public BillingService(
            StripeCustomerService stripeCustomerService,
            UserRepository userRepo,
            BackendConfig backendConfig,
            FrontendConfig frontendConfig,
            SubscriptionRepository subRepo
    ) {
        this.stripeCustomerService = stripeCustomerService;
        this.userRepo = userRepo;
        this.backendConfig = backendConfig;
        this.frontendConfig = frontendConfig;
        this.subRepo = subRepo;
    }

    /** 302 → Stripe Billing Portal */
    public ResponseEntity<Void> openPortal() throws Exception {
        UUID userId = UserContext.getUserId();
        String customerId = stripeCustomerService.getOrCreateCustomerId(userId);

        String returnUrl = frontendConfig.getFrontendOrigin() + "/app/account";

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customerId)
                .setReturnUrl(returnUrl)
                .build();

        Session session = Session.create(params);

        return redirectExternal(session.getUrl());
    }

    /** 302 → Stripe Checkout (subscription) */
    public ResponseEntity<Void> openCheckout(String cadence) throws Exception {
        UUID userId = UserContext.getUserId();
        String customerId = stripeCustomerService.getOrCreateCustomerId(userId);

        String priceId = switch (cadence) {
            case "monthly" -> enhancedMonthlyPrice;
            case "annual"  -> enhancedAnnualPrice;
            default -> throw new IllegalArgumentException("cadence must be monthly|annual");
        };

        String successUrl = backendConfig.getBackendOrigin()
                + "/api/billing/success?session_id={CHECKOUT_SESSION_ID}";
        String cancelUrl  = backendConfig.getBackendOrigin()
                + "/api/billing/cancel";

        com.stripe.param.checkout.SessionCreateParams params =
                com.stripe.param.checkout.SessionCreateParams.builder()
                        .setMode(com.stripe.param.checkout.SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customerId)
                        .addLineItem(
                                com.stripe.param.checkout.SessionCreateParams.LineItem.builder()
                                        .setPrice(priceId)
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setAllowPromotionCodes(true)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .build();

        com.stripe.model.checkout.Session session = com.stripe.model.checkout.Session.create(params);
        return redirectExternal(session.getUrl());
    }

    /** Stripe returns here after successful payment. Update user → 302 back to app. */
    public ResponseEntity<Void> handleCheckoutSuccess(String sessionId) throws Exception {
        var retrieve = com.stripe.param.checkout.SessionRetrieveParams.builder()
                .addExpand("subscription")
                .addExpand("subscription.items.data.price")
                .build();

        var session = com.stripe.model.checkout.Session.retrieve(sessionId, retrieve, null);
        if (!"subscription".equalsIgnoreCase(session.getMode())
                || !"paid".equalsIgnoreCase(session.getPaymentStatus())) {
            return redirectApp("/app/account?billing=unexpected");
        }

        String customerId = session.getCustomer();
        var userOpt = userRepo.findByStripeCustomerId(customerId);   // ensure this repo method exists
        if (userOpt.isEmpty()) return redirectApp("/app/account?billing=missing-user");

        User user = userOpt.get();
        var stripeSub = (com.stripe.model.Subscription) session.getSubscriptionObject();
        if (stripeSub == null) {
            stripeSub = com.stripe.model.Subscription.retrieve(session.getSubscription());
        }

        String subscriptionId = stripeSub.getId();
        String status = stripeSub.getStatus(); // active | trialing | past_due | canceled | …
        String priceId = null;
        if (stripeSub.getItems() != null && !stripeSub.getItems().getData().isEmpty()) {
            var item = stripeSub.getItems().getData().get(0);
            var price = item.getPrice();
            if (price != null) priceId = price.getId();
        }

        // 🔒 Idempotent upsert by externalSubscriptionId (avoid duplicates)
        var record = subRepo.findByStripeSubscriptionId(subscriptionId)
                .orElseGet(() -> Subscription.builder()
                        .userId(user.getId())
                        .stripeCustomerId(customerId)
                        .stripeSubscriptionId(subscriptionId)
                        .build());

        record.setPriceId(priceId);
        record.setStatus(status);
        subRepo.save(record);

        // Update user tier only when the sub is usable
        if ("active".equalsIgnoreCase(status) || "trialing".equalsIgnoreCase(status)) {
            if (!"ENHANCED".equalsIgnoreCase(user.getSubscriptionTier())) {
                user.setSubscriptionTier("ENHANCED");
                userRepo.save(user);
            }
        }

        return redirectApp("/app/account?billing=success");
    }

    /** 302 back to app on cancel */
    public ResponseEntity<Void> handleCheckoutCancel() {
        return redirectApp("/app/account?billing=cancel");
    }

    // ---- helpers -------------------------------------------------------------

    private ResponseEntity<Void> redirectExternal(String absoluteUrl) {
        return ResponseEntity.status(302).header(HttpHeaders.LOCATION, absoluteUrl).build();
    }

    private ResponseEntity<Void> redirectApp(String path) {
        String url = path.startsWith("http")
                ? path
                : frontendConfig.getFrontendOrigin() + path;
        return ResponseEntity.status(302).header(HttpHeaders.LOCATION, url).build();
    }
}