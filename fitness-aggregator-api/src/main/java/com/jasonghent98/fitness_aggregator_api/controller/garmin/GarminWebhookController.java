package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/garmin/webhook")
public class GarminWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GarminWebhookController.class);
    private final GarminWebhookService garminWebhookService;

    GarminWebhookController(GarminWebhookService garminWebhookService) {
        this.garminWebhookService = garminWebhookService;
    }

    @PostMapping("/activity")
    public ResponseEntity<Void> receiveActivity(@RequestBody GarminActivitySummaryPayload payload) {
        garminWebhookService.handleActivityEvents(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/daily")
    public ResponseEntity<Void> receiveDailies(@RequestBody GarminDailySummaryPayload payload) {
        garminWebhookService.handleDailyEvents(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hrvsummary")
    public ResponseEntity<Void> receiveHrvSummary(@RequestBody GarminHrvSummaryPayload payload) {
        garminWebhookService.handleHrvEvents(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/healthsummary")
    public ResponseEntity<Void> retrieveHealthSummary(@RequestBody GarminHealthSummaryPayload payload) {
        garminWebhookService.handleHealthEvents(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pulseox")
    public ResponseEntity<Void> receivePulseOx(@RequestBody GarminPulseOxSummaryPayload payload) {
        garminWebhookService.handlePulseOxEvents(payload);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/sleep")
    public ResponseEntity<Void> receiveSleep(@RequestBody GarminSleepSummaryPayload payload) {
        garminWebhookService.handleSleepEvents(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stress")
    public ResponseEntity<Void> receiveStress(@RequestBody GarminStressSummaryPayload payload) {
        garminWebhookService.handleStressEvents(payload);
        return ResponseEntity.ok().build();
    }
}
