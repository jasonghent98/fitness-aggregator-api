package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
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

    public GarminController(GarminService garminService) {
        this.garminService = garminService;
    }


    // Sleep data
    @GetMapping("/sleep")
    public ResponseEntity<List<GarminSleepSummary>> getGarminSleep(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestHeader(name = "X-Tier", defaultValue = "FREE") String tier) {

        int maxDays = tier.equalsIgnoreCase("ENHANCED") ? 90 : 30;
        UUID userId = UserContext.getUserId();
        GarminService.DateRange window = garminService.resolveRange(range, startDate, endDate, maxDays);

        List<GarminSleepSummary> results = garminService.getSleepForUserForGivenRange(userId.toString(), window.start(), window.end());
        return ResponseEntity.ok(results);
    }

    // Stress data
    @GetMapping("/stress")
    public ResponseEntity<List<GarminStressSummary>> getGarminStress(
            @RequestParam UUID userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return ResponseEntity.ok().build();
    }

    // HRV data
    @GetMapping("/hrv")
    public ResponseEntity<List<GarminHrvSummary>> getGarminHrv(
            @RequestParam UUID userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return ResponseEntity.ok().build();
    }

    // Daily summaries
    @GetMapping("/dailies")
    public ResponseEntity<List<GarminDailySummary>> getGarminDailies(
            @RequestParam UUID userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return ResponseEntity.ok().build();
    }

    // PulseOx
    @GetMapping("/pulseox")
    public ResponseEntity<List<GarminPulseOxSummary>> getGarminPulseOx(
            @RequestParam UUID userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return ResponseEntity.ok().build();
    }
}