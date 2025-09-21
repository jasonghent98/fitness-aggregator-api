package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.UserContextResolver;
import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/garmin")
public class GarminController {
    private final GarminService garminService;
    private final UserContextResolver userContextResolver;

    public GarminController(GarminService garminService, UserContextResolver userContextResolver) {
        this.garminService = garminService;
        this.userContextResolver = userContextResolver;
    }


    // Sleep data
    @GetMapping("/sleep")
    public ResponseEntity<List<GarminSleepSummary>> getGarminSleep(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
            ) {


        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();

        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminSleepSummary> results = garminService.getSleepForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok(results);
    }

    // Stress data
    @GetMapping("/stress")
    public ResponseEntity<List<GarminStressSummary>> getGarminStress(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminStressSummary> results = garminService.getStressForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok().build();
    }

    // HRV data
    @GetMapping("/hrv")
    public ResponseEntity<List<GarminHrvSummary>> getGarminHrv(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminHrvSummary> results = garminService.getHrvForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok().build();
    }

    // Daily summaries
    @GetMapping("/dailies")
    public ResponseEntity<List<GarminDailySummary>> getGarminDailies(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminDailySummary> results = garminService.getDailyForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok().build();
    }

    // PulseOx
    @GetMapping("/pulseox")
    public ResponseEntity<List<GarminPulseOxSummary>> getGarminPulseOx(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminPulseOxSummary> results = garminService.getPulseOxForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok().build();
    }
}