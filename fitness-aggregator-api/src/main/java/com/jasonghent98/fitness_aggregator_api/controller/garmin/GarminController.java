package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.UserContextResolver;
import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminService;
import com.jasonghent98.fitness_aggregator_api.util.DateRangeUtil;
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

    // Activity data
    @GetMapping("/activity")
    public ResponseEntity<List<GarminActivitySummary>> getGarminActivity(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {


        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();

        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminActivitySummary> results = garminService.getActivityForUserForGivenRange(userId, window.start(), window.end());
        return ResponseEntity.ok(results);
    }


    // Sleep data
    @GetMapping("/sleep")
    public ResponseEntity<List<GarminSleepSummary>> getGarminSleep(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
            ) {


        UUID userId = UserContext.getUserId();
        System.out.println("[DEBUG] Sleep endpoint - User ID: " + userId);
        System.out.println("[DEBUG] Sleep endpoint - Range: " + range);
        String subTier = userContextResolver.getSubscriptionTier();

        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);
        System.out.println("[DEBUG] Sleep endpoint - Date range: " + window.start() + " to " + window.end());

        List<GarminSleepSummary> results = garminService.getSleepForUserForGivenRange(userId, window.start(), window.end());
        System.out.println("[DEBUG] Sleep endpoint - Results count: " + results.size());
        return ResponseEntity.ok(results);
    }

    // Stress data
    @GetMapping("/stress")
    public ResponseEntity<List<GarminStressSummary>> getGarminStress(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminStressSummary> results = garminService.getStressForUserForGivenRange(userId, window.start(), window.end());
        return ResponseEntity.ok(results);
    }

    // HRV data
    @GetMapping("/hrv")
    public ResponseEntity<List<GarminHrvSummary>> getGarminHrv(
            @RequestParam(name ="range", required = false) String range,
            @RequestParam(name ="startDate", required = false) LocalDate startDate,
            @RequestParam(name ="endDate", required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminHrvSummary> results = garminService.getHrvForUserForGivenRange(userId, window.start(), window.end());
        return ResponseEntity.ok(results);
    }

    // Daily summaries
    @GetMapping("/dailies")
    public ResponseEntity<List<GarminDailySummary>> getGarminDailies(
            @RequestParam(name="range", required = false) String range,
            @RequestParam(name="startDate", required = false) LocalDate startDate,
            @RequestParam(name="endDate", required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminDailySummary> results = garminService.getDailyForUserForGivenRange(userId, window.start(), window.end());
        return ResponseEntity.ok(results);
    }

    // PulseOx
    @GetMapping("/pulseox")
    public ResponseEntity<List<GarminPulseOxSummary>> getGarminPulseOx(
            @RequestParam(name= "range", required = false) String range,
            @RequestParam(name= "startDate", required = false) LocalDate startDate,
            @RequestParam(name= "endDate", required = false) LocalDate endDate) {

        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();


        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminPulseOxSummary> results = garminService.getPulseOxForUserForGivenRange(userId, window.start(), window.end());
        return ResponseEntity.ok(results);
    }
}