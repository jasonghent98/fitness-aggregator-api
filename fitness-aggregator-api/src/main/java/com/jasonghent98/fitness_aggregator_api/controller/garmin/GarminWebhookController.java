package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminWebhookService;
import com.jasonghent98.fitness_aggregator_api.util.WebhookLogger;
import com.jasonghent98.fitness_aggregator_api.util.WebhookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/dailies")
    public ResponseEntity<Void> receiveDailies(@RequestBody GarminDailySummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin Daily");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Daily",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getDailySummaries().getFirst().getSummaryId(), p.getDailySummaries().getFirst().getUserId())
            );

            garminWebhookService.handleDailyEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Daily", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/hrvsummary")
    public ResponseEntity<Void> receiveHrvSummary(@RequestBody GarminHrvSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin HRV");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin HRV",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getHrvSummaries().getFirst().getSummaryId(), p.getHrvSummaries().getFirst().getUserId())
            );

            garminWebhookService.handleHrvEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin HRV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/healthsummary")
    public ResponseEntity<Void> retrieveHealthSummary(@RequestBody GarminHealthSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin Health");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Health",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getHealthSummaries().getFirst().getSummaryId(), p.getHealthSummaries().getFirst().getUserId())
            );

            garminWebhookService.handleHealthEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Health", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/pulseox")
    public ResponseEntity<Void> receivePulseOx(@RequestBody GarminPulseOxSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin Pulse Ox");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Pulse Ox",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getPulseOxSummaries().getFirst().getSummaryId(), p.getPulseOxSummaries().getFirst().getUserId())
            );

            garminWebhookService.handlePulseOxEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Pulse Ox", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/sleep")
    public ResponseEntity<Void> receiveSleep(@RequestBody GarminSleepSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin Sleep");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Sleep",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getSleepSummaries().getFirst().getSummaryId(), p.getSleepSummaries().getFirst().getUserId())
            );

            garminWebhookService.handleSleepEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Sleep", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/stress")
    public ResponseEntity<Void> receiveStress(@RequestBody GarminStressSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, "Garmin Stress");

            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Stress",
                    payload,
                    p -> String.format("summaryId=%s, userId=%s", p.getStressSummaries().getFirst().getSummaryId(), p.getStressSummaries().getFirst().getUserId())
            );

            garminWebhookService.handleStressEvents(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Stress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
