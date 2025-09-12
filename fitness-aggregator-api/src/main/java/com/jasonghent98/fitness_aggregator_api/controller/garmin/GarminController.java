package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/garmin")
@RequiredArgsConstructor
public class GarminController {


    // WEBHOOKS //

    private final GarminWebhookService garminWebhookService;

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody Map<String, Object> payload,
                                        @RequestHeader Map<String, String> headers) {
        // TODO: verify HMAC signature later using headers (X-...)
        garminWebhookService.handleEvent(payload);
        return ResponseEntity.noContent().build(); // 204 quick response
    }
}