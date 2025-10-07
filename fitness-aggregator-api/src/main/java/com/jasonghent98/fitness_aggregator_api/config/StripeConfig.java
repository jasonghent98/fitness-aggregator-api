package com.jasonghent98.fitness_aggregator_api.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // stripe-java uses a static apiKey by default
        Stripe.apiKey = secretKey;
    }
}