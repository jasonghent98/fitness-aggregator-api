package com.jasonghent98.fitness_aggregator_api.context;

import java.util.UUID;

public class UserContext {
    private static final ThreadLocal<UUID> userHolder = new ThreadLocal<>();

    public static void setUserId(UUID userId) {
        userHolder.set(userId);
    }

    public static UUID getUserId() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}