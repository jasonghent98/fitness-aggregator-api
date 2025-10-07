package com.jasonghent98.fitness_aggregator_api.controller;



import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.service.StripeCustomerService;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final StripeCustomerService stripeCustomerService;
    private final FrontendConfig frontendConfig;

    public BillingController(StripeCustomerService stripeCustomerService,
                             FrontendConfig frontendConfig) {
        this.stripeCustomerService = stripeCustomerService;
        this.frontendConfig = frontendConfig;
    }

    /**
     * Creates a Stripe Billing Portal session and redirects the browser to it.
     * Expects auth via your existing session (e.g., X-Actualize-Session header → UserContext).
     */
    @PostMapping("/portal")
    public ResponseEntity<Void> openPortal() throws Exception {
        UUID userId = UserContext.getUserId();

        // Ensure the user has a Stripe customer id (create if missing)
        String customerId = stripeCustomerService.getOrCreateCustomerId(userId);

        // Where Stripe will send the user after they exit the Portal
        String returnUrl = frontendConfig.getFrontendOrigin() + "/app/account";

        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomer(customerId)
                .setReturnUrl(returnUrl)
                .build();

        Session session = Session.create(params);

        // 302 to Stripe-hosted Portal
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, session.getUrl())
                .build();
    }
}