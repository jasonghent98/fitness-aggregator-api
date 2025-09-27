package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.*;
import com.jasonghent98.fitness_aggregator_api.repository.fitbit.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FitbitIngestionService {

    private final FitbitService fitbitService;
    private final FitbitActivityRepository activityRepo;
    private final FitbitSleepRepository sleepRepo;
    private final FitbitHeartRateRepository heartRateRepo;
    private final FitbitFoodLogRepository foodRepo;
    private final FitbitBodyRepository bodyRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public FitbitIngestionService(
            FitbitService fitbitService,
            FitbitActivityRepository activityRepo,
            FitbitSleepRepository sleepRepo,
            FitbitHeartRateRepository heartRateRepo,
            FitbitFoodLogRepository foodRepo,
            FitbitBodyRepository bodyRepo
    ) {
        this.fitbitService = fitbitService;
        this.activityRepo = activityRepo;
        this.sleepRepo = sleepRepo;
        this.heartRateRepo = heartRateRepo;
        this.foodRepo = foodRepo;
        this.bodyRepo = bodyRepo;
    }

    /**
     * Handles Fitbit webhook push event.
     * Parses event → decides what collection to refresh → fetches latest Fitbit data → persists.
     */
    @Transactional
    public void handleWebhook(String rawJson) {
        try {
            JsonNode root = mapper.readTree(rawJson);
            if (!root.isArray()) return;

            for (JsonNode ev : root) {
                String collectionType = ev.path("collectionType").asText();
                String fitbitUserId = ev.path("ownerId").asText();
                // SubId helps if you support multiple subscriptions per app
                // String subscriptionId = ev.path("subscriptionId").asText();

                switch (collectionType.toLowerCase()) {
                    case "activities" -> ingestActivities(fitbitUserId);
                    case "sleep" -> ingestSleep(fitbitUserId);
                    case "heart" -> ingestHeartRate(fitbitUserId);
                    case "foods" -> ingestFoods(fitbitUserId);
                    case "body" -> ingestBody(fitbitUserId);
                    default -> {
                        System.out.println("Unhandled Fitbit collection: " + collectionType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error handling Fitbit webhook", e);
        }
    }

    // ---------------- INGEST METHODS ---------------- //

    private void ingestActivities(String fitbitUserId) {
        List<FitbitActivity> activities = fitbitService.fetchUserActivities(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        // Save or upsert
        for (FitbitActivity act : activities) {
            activityRepo.save(act);
        }
    }

    private void ingestSleep(String fitbitUserId) {
        List<FitbitSleepLog> sleeps = fitbitService.fetchUserSleep(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        for (FitbitSleepLog s : sleeps) {
            sleepRepo.save(s);
        }
    }

    private void ingestHeartRate(String fitbitUserId) {
        List<FitbitHeartRateLog> hr = fitbitService.fetchUserHeartRate(fitbitUserId, LocalDate.now().minusDays(1), LocalDate.now());
        for (FitbitHeartRateLog h : hr) {
            heartRateRepo.save(h);
        }
    }

    private void ingestFoods(String fitbitUserId) {
        List<FitbitFoodLog> foods = fitbitService.fetchUserFoods(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        for (FitbitFoodLog f : foods) {
            foodRepo.save(f);
        }
    }

    private void ingestBody(String fitbitUserId) {
        List<FitbitBodyLog> bodyLogs = fitbitService.fetchUserBody(fitbitUserId, LocalDate.now().minusDays(30), LocalDate.now());
        for (FitbitBodyLog b : bodyLogs) {
            bodyRepo.save(b);
        }
    }
}