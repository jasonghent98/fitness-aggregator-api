
package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import com.jasonghent98.fitness_aggregator_api.dto.fitbit.*;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.*;
import com.jasonghent98.fitness_aggregator_api.repository.fitbit.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FitbitIngestionService {

    private final FitbitService fitbitService;
    private final FitbitActivityRepository activityRepo;
    private final FitbitSleepRepository sleepRepo;
    private final FitbitHRRepository heartRateRepo;
    private final FitbitFoodRepository foodRepo;
    private final FitbitBodyRepository bodyRepo;
    private final ProviderAccountService providerAccountServ;
    private final FitbitMapper fitbitMapper;
    private final ObjectMapper mapper = new ObjectMapper();

    public FitbitIngestionService(
            FitbitService fitbitService,
            FitbitActivityRepository activityRepo,
            FitbitSleepRepository sleepRepo,
            FitbitHRRepository heartRateRepo,
            FitbitFoodRepository foodRepo,
            FitbitBodyRepository bodyRepo,
            ProviderAccountService providerAccountServ,
            FitbitMapper fitbitMapper
    ) {
        this.fitbitService = fitbitService;
        this.activityRepo = activityRepo;
        this.sleepRepo = sleepRepo;
        this.heartRateRepo = heartRateRepo;
        this.foodRepo = foodRepo;
        this.bodyRepo = bodyRepo;
        this.providerAccountServ = providerAccountServ;
        this.fitbitMapper = fitbitMapper;
    }


    @Transactional
    @Async
    public void handleWebhook(String rawJson) {
        try {
            JsonNode root = mapper.readTree(rawJson);
            if (!root.isArray()) return;

            for (JsonNode ev : root) {
                String collectionType = ev.path("collectionType").asText();
                String fitbitUserId = ev.path("ownerId").asText();
                LocalDate date = LocalDate.parse(ev.path("date").asText());
                // SubId helps if you support multiple subscriptions per app
                // String subscriptionId = ev.path("subscriptionId").asText();

                switch (collectionType.toLowerCase()) {
                    case "activities" -> ingestActivity(fitbitUserId, date);
                    case "sleep" -> ingestSleep(fitbitUserId, date);
                    case "foods" -> ingestFood(fitbitUserId, date);
                    case "body" -> ingestBody(fitbitUserId, date);
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


    private void ingestActivity(String fitbitUserId,  LocalDate date) {
        String fitbitAccessToken = providerAccountServ.getValidAccessToken(fitbitUserId, "fitbit");
        ProviderAccount providerAcc = providerAccountServ.getProviderAccountForProviderUserAndProvider("fitbit", fitbitUserId);
        FitbitActivityLog actRes = fitbitService.fetchDailyActivity(
                fitbitAccessToken,
                fitbitUserId,
                date
        );
        // Save or upsert
        UUID actualizeUserId = providerAcc.getUser().getId();
        FitbitActivitySummary fas = fitbitMapper.mapActivityPayload(actualizeUserId, fitbitUserId, date, actRes.getActivities().getFirst());
        activityRepo.save(fas);

        System.out.println(actRes + " FROM INGEST ACTIVITY!!");
    }



    private void ingestSleep(String fitbitUserId, LocalDate date) {
        String fitbitAccessToken = providerAccountServ.getValidAccessToken(fitbitUserId, "fitbit");
        ProviderAccount providerAcc = providerAccountServ.getProviderAccountForProviderUserAndProvider("fitbit", fitbitUserId);
        FitbitSleepLog sleepRes = fitbitService.fetchDailySleep(
                fitbitAccessToken,
                fitbitUserId,
                date
        );
        // Save or upsert
        UUID actualizeUserId = providerAcc.getUser().getId();
        FitbitSleepSummary sleepSummary = fitbitMapper.mapSleepPayload(actualizeUserId, fitbitUserId, sleepRes.getSleep().getFirst());
        sleepRepo.save(sleepSummary);

        System.out.println(sleepRes + " FROM INGEST SLEEP!!");
    }

    private void ingestFood(String fitbitUserId, LocalDate date) {
        String fitbitAccessToken = providerAccountServ.getValidAccessToken(fitbitUserId, "fitbit");
        ProviderAccount providerAcc = providerAccountServ.getProviderAccountForProviderUserAndProvider("fitbit", fitbitUserId);
        FitbitFoodLog foodRes = fitbitService.fetchDailyFood(fitbitAccessToken, fitbitUserId, LocalDate.now());
        // Save or upsert
        UUID actualizeUserId = providerAcc.getUser().getId();
        FitbitFoodSummary foodSummary = fitbitMapper.mapFoodPayload(actualizeUserId, fitbitUserId, foodRes.getFoods().getFirst());
        foodRepo.save(foodSummary);

        System.out.println(foodRes + " FROM INGEST FOOD!!");


    }

    private void ingestBody(String fitbitUserId, LocalDate date) {
        String fitbitAccessToken = providerAccountServ.getValidAccessToken(fitbitUserId, "fitbit");
        ProviderAccount providerAcc = providerAccountServ.getProviderAccountForProviderUserAndProvider("fitbit", fitbitUserId);
        FitbitBodyLog bodyRes = fitbitService.fetchDailyBody(fitbitAccessToken, fitbitUserId, LocalDate.now());
        // Save or upsert
        UUID actualizeUserId = providerAcc.getUser().getId();
        FitbitBodySummary bodySummary = fitbitMapper.mapBodyPayload(actualizeUserId, fitbitUserId, bodyRes.getWeight().getFirst());
        bodyRepo.save(bodySummary);

        System.out.println(bodyRes + " FROM INGEST BODY!!");

    }

    /*
    private void ingestHeartRate(String fitbitUserId) {
        List<FitbitHeartRateLog> hr = fitbitService.fetchUserHeartRate(fitbitUserId, LocalDate.now().minusDays(1), LocalDate.now());
        for (FitbitHeartRateLog h : hr) {

        }
    }
     */

    // ---------------- UPSERT METHODS ---------------- //



}
