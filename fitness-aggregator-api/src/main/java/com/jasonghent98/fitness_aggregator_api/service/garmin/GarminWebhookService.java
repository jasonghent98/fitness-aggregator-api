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
    public void handleDailyEvents(List<GarminDailySummaryPayload> payloads) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting {} Garmin Daily payloads", payloads.size());

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
    public void handleHrvEvents(List<GarminHrvSummaryPayload> payloads) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting {} Garmin HRV payloads", payloads.size());

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
    public void handleHealthEvents(List<GarminHealthSummaryPayload> payloads) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting {} Garmin Health payloads", payloads.size());

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
    public void handlePulseOxEvents(List<GarminPulseOxSummaryPayload> payloads) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting {} Garmin Pulse Ox payloads", payloads.size());

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
    public void handleSleepEvents(List<GarminSleepSummaryPayload> payloads) {
        try {
            // ✅ map DTOs -> entities
            // ✅ persist with repository.saveAll(...)
            log.info("Persisting {} Garmin Sleep payloads", payloads.size());

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
    public void handleStressEvents(List<GarminStressSummaryPayload> payloads) {
        try {
            log.info("Persisting {} Garmin Stress payloads", payloads.size());
            // map & persist
        } catch (Exception e) {
            log.error("Error persisting Garmin Stress payloads", e);
        }
    }

    // 👉 Repeat for HRV, Respiration, PulseOx, etc.
}