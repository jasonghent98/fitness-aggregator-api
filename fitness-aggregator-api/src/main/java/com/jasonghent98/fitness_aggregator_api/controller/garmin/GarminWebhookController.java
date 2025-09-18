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

import java.util.List;


@RestController
@RequestMapping("/api/garmin/webhook")
public class GarminWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GarminWebhookController.class);
    private final GarminWebhookService garminWebhookService;

    GarminWebhookController(GarminWebhookService garminWebhookService) {
        this.garminWebhookService = garminWebhookService;
    }

    @PostMapping("/dailies")
    public ResponseEntity<Void> receiveDailies(@RequestBody List<GarminDailySummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin Daily");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Daily",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handleDailyEvents(payloads);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Daily", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/hrvsummary")
    public ResponseEntity<Void> receiveHrvSummary(@RequestBody List<GarminHrvSummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin HRV");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin HRV",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handleHrvEvents(payloads);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin HRV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/healthsummary")
    public ResponseEntity<Void> retrieveHealthSummary(@RequestBody List<GarminHealthSummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin Health");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Health",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handleHealthEvents(payloads);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Health", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/pulseox")
    public ResponseEntity<Void> receivePulseOx(@RequestBody List<GarminPulseOxSummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin Pulse Ox");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Pulse Ox",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handlePulseOxEvents(payloads);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Pulse Ox", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/sleep")
    public ResponseEntity<Void> receiveSleep(@RequestBody List<GarminSleepSummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin Sleep");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Sleep",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handleSleepEvents(payloads);



            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Sleep", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/stress")
    public ResponseEntity<Void> receiveStress(@RequestBody List<GarminStressSummaryPayload> payloads) {
        try {
            // Make sure no empty payloads
            WebhookValidator.requireNonEmpty(payloads, "Garmin Stress");

            // log event
            WebhookLogger.logWebhookEvent(
                    log,
                    "Garmin Stress",
                    payloads,
                    p -> String.format("summaryId=%s, userId=%s", p.getSummaryId(), p.getUserId())
            );

            /* TODO: Persist to DB via async processing enqueue */
            garminWebhookService.handleStressEvents(payloads);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            WebhookLogger.logWebhookError(log, "Garmin Stress", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
