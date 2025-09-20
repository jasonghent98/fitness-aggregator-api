package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarminWebhookService {

    private static final Logger log = LoggerFactory.getLogger(GarminWebhookService.class);

    @Async
    public void handleDailyEvents(GarminDailySummaryPayload payload) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting Garmin Daily payloads");
            System.out.println(payload + " DAILY EVENT ");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Daily payloads", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHrvEvents(GarminHrvSummaryPayload payload) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting Garmin HRV payload");
            System.out.println(payload + " HRV EVENT ");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin HRV payloads", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHealthEvents(GarminHealthSummaryPayload payload) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting Garmin Health payload");
            System.out.println(payload + " HEALTH EVENT ");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Health payloads", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handlePulseOxEvents(GarminPulseOxSummaryPayload payload) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting Garmin Pulse Ox payload");
            System.out.println(payload + " PULSE OX EVENT ");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Pulse Ox payloads", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleSleepEvents(GarminSleepSummaryPayload payload) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting Garmin Sleep payloads");
            System.out.println(payload + " SLEEP EVENT");

            // Example:
            // List<SleepEntity> entities = payloads.stream()
            //     .map(this::mapToEntity)
            //     .toList();
            // sleepRepository.saveAll(entities);

        } catch (Exception e) {
            log.error("Error persisting Garmin Sleep payloads", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleStressEvents(GarminStressSummaryPayload payload) {
        try {
            log.info("Persisting Garmin Stress payloads");
            System.out.println(payload + " FROM STRESS EVENT");
            // map & persist
        } catch (Exception e) {
            log.error("Error persisting Garmin Stress payloads", e);
        }
    }

    // 👉 Repeat for HRV, Respiration, PulseOx, etc.
}