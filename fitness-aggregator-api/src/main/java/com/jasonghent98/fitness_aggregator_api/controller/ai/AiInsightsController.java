package com.jasonghent98.fitness_aggregator_api.controller.ai;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.UserContextResolver;
import com.jasonghent98.fitness_aggregator_api.model.ai.AiInsightCache;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminHrvSummary;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import com.jasonghent98.fitness_aggregator_api.repository.ai.AiInsightCacheRepository;
import com.jasonghent98.fitness_aggregator_api.service.ai.*;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminService;
import com.jasonghent98.fitness_aggregator_api.util.DateRangeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
public class AiInsightsController {

    private static final Logger log = LoggerFactory.getLogger(AiInsightsController.class);

    private final SleepInsightsService sleepInsightsService;
    private final StepsInsightsService stepsInsightsService;
    private final CaloriesInsightsService caloriesInsightsService;
    private final HeartRateInsightsService heartRateInsightsService;
    private final HrvInsightsService hrvInsightsService;
    private final GarminService garminService;
    private final UserContextResolver userContextResolver;
    private final AiInsightCacheRepository cacheRepository;

    public AiInsightsController(
            SleepInsightsService sleepInsightsService,
            StepsInsightsService stepsInsightsService,
            CaloriesInsightsService caloriesInsightsService,
            HeartRateInsightsService heartRateInsightsService,
            HrvInsightsService hrvInsightsService,
            GarminService garminService,
            UserContextResolver userContextResolver,
            AiInsightCacheRepository cacheRepository
    ) {
        this.sleepInsightsService = sleepInsightsService;
        this.stepsInsightsService = stepsInsightsService;
        this.caloriesInsightsService = caloriesInsightsService;
        this.heartRateInsightsService = heartRateInsightsService;
        this.hrvInsightsService = hrvInsightsService;
        this.garminService = garminService;
        this.userContextResolver = userContextResolver;
        this.cacheRepository = cacheRepository;
    }

    /**
     * Generate AI insights for sleep data
     * GET /api/ai/sleep-insights?range=7d
     * or
     * GET /api/ai/sleep-insights?startDate=2024-01-01&endDate=2024-01-07
     *
     * Caches insights per user per day to avoid redundant LLM calls.
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
        LocalDate today = LocalDate.now();

        // Check cache first - one insight per user per day
        Optional<AiInsightCache> cachedInsight = cacheRepository.findByUserIdAndMetricTypeAndCacheDate(
                userId, "sleep", today
        );

        if (cachedInsight.isPresent()) {
            log.info("[AI Cache] CACHE HIT - Returning cached sleep insight for user {} (cached at {})",
                    userId, cachedInsight.get().getCreatedAt());

            Map<String, String> response = new HashMap<>();
            response.put("insight", cachedInsight.get().getInsight());
            response.put("cached", "true");
            response.put("cachedAt", cachedInsight.get().getCreatedAt().toString());
            return ResponseEntity.ok(response);
        }

        log.info("[AI Cache] CACHE MISS - Generating new sleep insight for user {}", userId);

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

        // Cache the insight for today
        try {
            AiInsightCache cache = AiInsightCache.builder()
                    .userId(userId)
                    .metricType("sleep")
                    .cacheDate(today)
                    .insight(insight)
                    .build();
            cacheRepository.save(cache);
            log.info("[AI Cache] Saved new sleep insight to cache for user {}", userId);
        } catch (Exception e) {
            // Don't fail the request if caching fails
            log.warn("[AI Cache] Failed to save insight to cache: {}", e.getMessage());
        }

        // Return response
        Map<String, String> response = new HashMap<>();
        response.put("insight", insight);
        response.put("dataPoints", String.valueOf(sleepData.size()));
        response.put("startDate", window.start().toString());
        response.put("endDate", window.end().toString());
        response.put("cached", "false");

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
