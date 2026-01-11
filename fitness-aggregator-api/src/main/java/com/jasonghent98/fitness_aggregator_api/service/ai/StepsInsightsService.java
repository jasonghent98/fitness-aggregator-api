package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StepsInsightsService {

    private final GeminiService geminiService;

    public StepsInsightsService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate AI insights for steps data
     * @param dailiesData List of daily summaries
     * @return AI-generated insight text
     */
    public String generateStepsInsights(List<GarminDailySummary> dailiesData) {
        if (dailiesData == null || dailiesData.isEmpty()) {
            return "No steps data available to analyze.";
        }

        // Format steps data for the prompt
        String formattedData = formatStepsData(dailiesData);

        // Create the prompt for Gemini
        String prompt = buildStepsInsightPrompt(formattedData, dailiesData.size());

        // Generate insights using Gemini
        return geminiService.generateContent(prompt);
    }

    /**
     * Format steps data into a readable string for the AI
     */
    private String formatStepsData(List<GarminDailySummary> dailiesData) {
        return dailiesData.stream()
                .map(daily -> {
                    Integer steps = daily.getSteps();
                    Integer stepsGoal = daily.getStepsGoal();
                    Double distanceInMeters = daily.getDistanceInMeters();
                    Integer floorsClimbed = daily.getFloorsClimbed();

                    double distanceKm = distanceInMeters != null ? distanceInMeters / 1000.0 : 0;
                    double goalProgress = (stepsGoal != null && stepsGoal > 0 && steps != null)
                            ? (steps * 100.0 / stepsGoal) : 0;

                    return String.format(
                            "Date: %s, Steps: %d, Goal: %d, Progress: %.0f%%, Distance: %.1f km, Floors: %d",
                            daily.getCalendarDate(),
                            steps != null ? steps : 0,
                            stepsGoal != null ? stepsGoal : 0,
                            goalProgress,
                            distanceKm,
                            floorsClimbed != null ? floorsClimbed : 0
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Build a comprehensive prompt for Gemini to analyze steps data
     */
    private String buildStepsInsightPrompt(String formattedData, int dataPoints) {
        return String.format("""
                You are a fitness activity analyst. Analyze the following steps data from the past %d days and provide a concise, actionable insight in 2-3 sentences.

                Focus on:
                1. Overall activity trends (increasing, decreasing, or consistent)
                2. Goal achievement patterns
                3. One specific, actionable recommendation to improve daily activity

                Steps Data:
                %s

                Provide a friendly, encouraging insight that helps the user understand their activity patterns. Keep it under 100 words.
                """, dataPoints, formattedData);
    }
}
