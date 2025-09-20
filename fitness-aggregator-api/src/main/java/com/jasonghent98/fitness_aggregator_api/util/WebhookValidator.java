package com.jasonghent98.fitness_aggregator_api.util;

import java.util.List;

public class WebhookValidator {
    public static <T> void requireNonEmpty(T payload, String eventName) {
        if (payload == null) {
            throw new IllegalArgumentException(eventName + " webhook payload was empty");
        }
    }
}