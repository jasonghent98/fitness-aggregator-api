
package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import com.jasonghent98.fitness_aggregator_api.dto.fitbit.*;
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
    private final FitbitHRRepository heartRateRepo;
    private final FitbitFoodRepository foodRepo;
    private final FitbitBodyRepository bodyRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public FitbitIngestionService(
            FitbitService fitbitService,
            FitbitActivityRepository activityRepo,
            FitbitSleepRepository sleepRepo,
            FitbitHRRepository heartRateRepo,
            FitbitFoodRepository foodRepo,
            FitbitBodyRepository bodyRepo
    ) {
        this.fitbitService = fitbitService;
        this.activityRepo = activityRepo;
        this.sleepRepo = sleepRepo;
        this.heartRateRepo = heartRateRepo;
        this.foodRepo = foodRepo;
        this.bodyRepo = bodyRepo;
    }

/*
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
        List<FitbitActivityLog> activities = fitbitService.fetchUserActivities(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        // Save or upsert
        for (FitbitActivityLog act : activities) {
            activityRepo.save(act);
        }
    }



    private void ingestSleep(String fitbitUserId) {
       List<FitbitSleepLog> sleeps = fitbitService.fetchUserSleep(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        for (FitbitSleepLog s : sleeps) {

        }
    }

    private void ingestHeartRate(String fitbitUserId) {
       List<FitbitHeartRateLog> hr = fitbitService.fetchUserHeartRate(fitbitUserId, LocalDate.now().minusDays(1), LocalDate.now());
        for (FitbitHeartRateLog h : hr) {

        }
    }

    private void ingestFoods(String fitbitUserId) {
        List<FitbitFoodLog> foods = fitbitService.fetchUserFoods(fitbitUserId, LocalDate.now().minusDays(7), LocalDate.now());
        for (FitbitFoodLog f : foods) {

        }
    }

    private void ingestBody(String fitbitUserId) {
        List<FitbitBodyLog> bodyLogs = fitbitService.fetchUserBody(fitbitUserId, LocalDate.now().minusDays(30), LocalDate.now());
        for (FitbitBodyLog b : bodyLogs) {

        }
    }

     */
}
