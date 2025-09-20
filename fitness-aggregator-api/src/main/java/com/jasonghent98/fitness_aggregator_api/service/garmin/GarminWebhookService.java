package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.repository.garmin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarminWebhookService {

    private static final Logger log = LoggerFactory.getLogger(GarminWebhookService.class);
    private final GarminDailyRepository garminDailyRepo;
    private final GarminSleepRepository garminSleepRepo;
    private final GarminStressRepository garminStressRepo;
    private final GarminHrvRepository garminHrvRepo;
    private final GarminPulseOxRepository garminPulseOxRepo;


    GarminWebhookService(
            GarminDailyRepository garminDailyRepo,
            GarminSleepRepository garminSleepRepo,
            GarminStressRepository garminStressRepo,
            GarminHrvRepository garminHrvRepo,
            GarminPulseOxRepository garminPulseOxRepo
    ) {
        this.garminDailyRepo = garminDailyRepo;
        this.garminSleepRepo = garminSleepRepo;
        this.garminStressRepo = garminStressRepo;
        this.garminHrvRepo = garminHrvRepo;
        this.garminPulseOxRepo = garminPulseOxRepo;
    }

    @Async
    public void handleDailyEvents(GarminDailySummaryPayload payload) {
        try {
            log.info("Persisting Garmin Daily payload");


        } catch (Exception e) {
            log.error("Error persisting Garmin Daily payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHrvEvents(GarminHrvSummaryPayload payload) {
        try {
            log.info("Persisting Garmin HRV payload");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin HRV payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHealthEvents(GarminHealthSummaryPayload payload) {
        try {
            log.info("Persisting Garmin Health payload");


            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Health payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handlePulseOxEvents(GarminPulseOxSummaryPayload payload) {
        try {
            log.info("Persisting Garmin Pulse Ox payload");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Pulse Ox payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleSleepEvents(GarminSleepSummaryPayload payload) {
        try {
            log.info("Persisting Garmin Sleep payloads");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Sleep payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleStressEvents(GarminStressSummaryPayload payload) {
        try {
            log.info("Persisting Garmin Stress payloads");
            // map & persist

        } catch (Exception e) {
            log.error("Error persisting Garmin Stress payloads", e);
        }
    }

    // 👉 Repeat for HRV, Respiration, PulseOx, etc.
}