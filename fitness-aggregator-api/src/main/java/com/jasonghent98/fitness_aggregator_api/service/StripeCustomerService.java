package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class StripeCustomerService {

    private final UserRepository userRepo;

    public StripeCustomerService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Returns a Stripe customer id for the user, creating one if needed and persisting it.
     */

    public String getOrCreateCustomerId(UUID userId) throws Exception {
        User u = userRepo.findById(userId).orElseThrow();
        if (u.getStripeCustomerId() != null && !u.getStripeCustomerId().isBlank()) {
            return u.getStripeCustomerId();
        }

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(u.getEmail())
                .putAllMetadata(Map.of("userId", userId.toString()))
                .build();

        Customer created = Customer.create(params);
        u.setStripeCustomerId(created.getId());
        userRepo.save(u);

        return created.getId();
    }


}