package com.jasonghent98.fitness_aggregator_api.context;

import java.util.UUID;

public class UserContext {
    private static final ThreadLocal<UUID> userHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> tierHolder = new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        userHolder.set(userId);
    }

    public static UUID getUserId() {
        return userHolder.get();
    }

    public static void setTier(String tier) {
        tierHolder.set(tier);
    }

    public static String getTier() {
        return tierHolder.get();
    }

    public static void clear() {
        tierHolder.remove();
        userHolder.remove();
    }

}