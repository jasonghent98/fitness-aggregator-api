package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billing;

    public BillingController(BillingService billing) {
        this.billing = billing;
    }

    @PostMapping("/portal")
    public ResponseEntity<Void> openPortal() throws Exception {
        return billing.openPortal();
    }

    record CheckoutReq(String cadence) {}

    @PostMapping("/checkout")
    public ResponseEntity<Void> openCheckout(@RequestBody CheckoutReq body) throws Exception {
        return billing.openCheckout(body.cadence());
    }

    @GetMapping("/success")
    public ResponseEntity<Void> success(@RequestParam("session_id") String sessionId) throws Exception {
        return billing.handleCheckoutSuccess(sessionId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancel() {
        return billing.handleCheckoutCancel();
    }
}