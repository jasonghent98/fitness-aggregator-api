package com.jasonghent98.fitness_aggregator_api.util;

import org.slf4j.Logger;

import java.util.List;
import java.util.function.Function;

public class WebhookLogger {

    /**
     * Logs a webhook event with consistent formatting.
     *
     * @param log the logger from the calling class
     * @param eventName a label for the webhook (e.g. "Garmin Sleep", "Garmin Stress")
     * @param payload the list of payloads received
     * @param summarizer function to extract a short summary string from a payload (for debug logging)
     */
    public static <T> void logWebhookEvent(
            Logger log,
            String eventName,
            T payload,
            Function<T, String> summarizer
    ) {
        // Always log count at INFO
        log.info("Received {} payload(s)", eventName);

        // Only log details if DEBUG is enabled
        if (log.isDebugEnabled()) {
            log.debug("{} payload details: {}", eventName, summarizer.apply(payload));
        }
    }

    /**
     * Logs an error in a consistent way.
     *
     * @param log the logger from the calling class
     * @param eventName webhook name
     * @param e exception to log
     */
    public static void logWebhookError(Logger log, String eventName, Exception e) {
        log.error("Failed to process {} webhook", eventName, e);
    }
}