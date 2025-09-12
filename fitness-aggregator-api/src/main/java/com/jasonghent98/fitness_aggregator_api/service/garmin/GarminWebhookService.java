package com.jasonghent98.fitness_aggregator_api.service.garmin;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class GarminWebhookService {
    public void handleEvent(Map<String, Object> payload) {
        // TODO: parse notification, enqueue sync job
    }
}
