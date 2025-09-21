package com.jasonghent98.fitness_aggregator_api.context;

import com.jasonghent98.fitness_aggregator_api.service.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserContextResolver {

    private final UserService userService;

    public UserContextResolver(UserService userService) {
        this.userService = userService;
    }

    public UUID getUserId() {
        return UserContext.getUserId();
    }

    public String getSubscriptionTier() {
        String subTier = UserContext.getTier();
        if (subTier == null || subTier.isBlank()) {
            // fallback: query DB
            UUID userId = getUserId();
            if (userId != null) {
                subTier = userService.findTierForUser(userId);
                UserContext.setTier(subTier); // cache it for the rest of this request
            } else {
                subTier = "FREE"; // last-resort default
            }
        }
        return subTier;
    }
}