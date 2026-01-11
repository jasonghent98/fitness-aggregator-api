package com.jasonghent98.fitness_aggregator_api.controller.ai;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.UserContextResolver;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminHrvSummary;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import com.jasonghent98.fitness_aggregator_api.service.ai.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminService;
import com.jasonghent98.fitness_aggregator_api.util.DateRangeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
public class AiInsightsController {

    private final SleepInsightsService sleepInsightsService;
    private final StepsInsightsService stepsInsightsService;
    private final CaloriesInsightsService caloriesInsightsService;
    private final HeartRateInsightsService heartRateInsightsService;
    private final HrvInsightsService hrvInsightsService;
    private final GarminService garminService;
    private final UserContextResolver userContextResolver;

    public AiInsightsController(
            SleepInsightsService sleepInsightsService,
            StepsInsightsService stepsInsightsService,
            CaloriesInsightsService caloriesInsightsService,
            HeartRateInsightsService heartRateInsightsService,
            HrvInsightsService hrvInsightsService,
            GarminService garminService,
            UserContextResolver userContextResolver
    ) {
        this.sleepInsightsService = sleepInsightsService;
        this.stepsInsightsService = stepsInsightsService;
        this.caloriesInsightsService = caloriesInsightsService;
        this.heartRateInsightsService = heartRateInsightsService;
        this.hrvInsightsService = hrvInsightsService;
        this.garminService = garminService;
        this.userContextResolver = userContextResolver;
    }

    /**
     * Generate AI insights for sleep data
     * GET /api/ai/sleep-insights?range=7d
     * or
     * GET /api/ai/sleep-insights?startDate=2024-01-01&endDate=2024-01-07
     */
    @GetMapping("/sleep-insights")
    public ResponseEntity<Map<String, String>> getSleepInsights(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        // Get user context
        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();

        // Determine date range
        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);

        // Fetch sleep data for the user
        List<GarminSleepSummary> sleepData = garminService.getSleepForUserForGivenRange(
                userId,
                window.start(),
                window.end()
        );

        // Generate AI insights
        String insight = sleepInsightsService.generateSleepInsights(sleepData);

        // Return response
        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(sleepData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Generate AI insights for steps data
     * GET /api/ai/steps-insights?range=7d
     */
    @GetMapping("/steps-insights")
    public ResponseEntity<Map<String, String>> getStepsInsights(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();
        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);

        List<GarminDailySummary> dailiesData = garminService.getDailyForUserForGivenRange(
                userId, window.start(), window.end()
        );

        String insight = stepsInsightsService.generateStepsInsights(dailiesData);

        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(dailiesData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Generate AI insights for calories data
     * GET /api/ai/calories-insights?range=7d
     */
    @GetMapping("/calories-insights")
    public ResponseEntity<Map<String, String>> getCaloriesInsights(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();
        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);

        List<GarminDailySummary> dailiesData = garminService.getDailyForUserForGivenRange(
                userId, window.start(), window.end()
        );

        String insight = caloriesInsightsService.generateCaloriesInsights(dailiesData);

        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(dailiesData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Generate AI insights for heart rate data
     * GET /api/ai/heartrate-insights?range=7d
     */
    @GetMapping("/heartrate-insights")
    public ResponseEntity<Map<String, String>> getHeartRateInsights(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();
        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);

        List<GarminDailySummary> dailiesData = garminService.getDailyForUserForGivenRange(
                userId, window.start(), window.end()
        );

        String insight = heartRateInsightsService.generateHeartRateInsights(dailiesData);

        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(dailiesData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Generate AI insights for HRV data
     * GET /api/ai/hrv-insights?range=7d
     */
    @GetMapping("/hrv-insights")
    public ResponseEntity<Map<String, String>> getHrvInsights(
            @RequestParam(name = "range", required = false) String range,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        UUID userId = UserContext.getUserId();
        String subTier = userContextResolver.getSubscriptionTier();
        int maxDays = subTier.equalsIgnoreCase("ENHANCED") ? 90 : (subTier.equalsIgnoreCase("ELITE") ? 365 : 30);
        DateRangeUtil.DateRange window = DateRangeUtil.resolve(range, startDate, endDate, maxDays);

        List<GarminHrvSummary> hrvData = garminService.getHrvForUserForGivenRange(
                userId, window.start(), window.end()
        );

        String insight = hrvInsightsService.generateHrvInsights(hrvData);

        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(hrvData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());

        return ResponseEntity.ok(response);
    }
}
