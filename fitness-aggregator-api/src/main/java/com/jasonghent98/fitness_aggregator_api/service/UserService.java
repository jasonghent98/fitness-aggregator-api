package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User upsertByEmail(String rawEmail) {
        String email = normalize(rawEmail);
        return userRepo.findByEmailIgnoreCase(email)
                .map(existing -> {
                    // Apply updates if needed
                    return userRepo.save(existing);
                })
                .orElseGet(() -> {
                    // Create new user
                    User u = new User();
                    u.setEmail(email);
                    u.setSubscriptionTier("FREE");
                    return userRepo.save(u);
                });
    }

    /**
     * Resolves the subscription tier for a given user.
     * Falls back to "FREE" if no tier is stored.
     */
    public String findTierForUser(UUID userId) {
        return userRepo.findById(userId)
                .map(User::getSubscriptionTier)
                .orElse("FREE");
    }


    private String normalize(String e) {
        if (e == null) throw new IllegalArgumentException("email is required");
        return e.trim();
    }
}
