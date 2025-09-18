package com.jasonghent98.fitness_aggregator_api.util;

import java.util.List;

public class WebhookValidator {
    public static <T> void requireNonEmpty(List<T> payloads, String eventName) {
        if (payloads == null || payloads.isEmpty()) {
            throw new IllegalArgumentException(eventName + " webhook payload was empty");
        }
    }
}