package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaloriesInsightsService {

    private final GeminiService geminiService;

    public CaloriesInsightsService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate AI insights for calories data
     * @param dailiesData List of daily summaries
     * @return AI-generated insight text
     */
    public String generateCaloriesInsights(List<GarminDailySummary> dailiesData) {
        if (dailiesData == null || dailiesData.isEmpty()) {
            return "No calories data available to analyze.";
        }

        // Format calories data for the prompt
        String formattedData = formatCaloriesData(dailiesData);

        // Create the prompt for Gemini
        String prompt = buildCaloriesInsightPrompt(formattedData, dailiesData.size());

        // Generate insights using Gemini
        return geminiService.generateContent(prompt);
    }

    /**
     * Format calories data into a readable string for the AI
     */
    private String formatCaloriesData(List<GarminDailySummary> dailiesData) {
        return dailiesData.stream()
                .map(daily -> {
                    Integer activeKcal = daily.getActiveKilocalories();
                    Integer bmrKcal = daily.getBmrKilocalories();
                    Integer activeTimeSeconds = daily.getActiveTimeInSeconds();

                    int totalKcal = (activeKcal != null ? activeKcal : 0) + (bmrKcal != null ? bmrKcal : 0);
                    int activeMinutes = activeTimeSeconds != null ? activeTimeSeconds / 60 : 0;

                    return String.format(
                            "Date: %s, Total: %d kcal, Active: %d kcal, BMR: %d kcal, Active Time: %d min",
                            daily.getCalendarDate(),
                            totalKcal,
                            activeKcal != null ? activeKcal : 0,
                            bmrKcal != null ? bmrKcal : 0,
                            activeMinutes
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Build a comprehensive prompt for Gemini to analyze calories data
     */
    private String buildCaloriesInsightPrompt(String formattedData, int dataPoints) {
        return String.format("""
                You are a fitness and nutrition analyst. Analyze the following calories data from the past %d days and provide a concise, actionable insight in 2-3 sentences.

                Focus on:
                1. Overall calorie burn trends and consistency
                2. Balance between active calories and BMR
                3. One specific, actionable recommendation to optimize energy expenditure

                Calories Data:
                %s

                Provide a friendly, encouraging insight that helps the user understand their calorie patterns. Keep it under 100 words.
                """, dataPoints, formattedData);
    }
}
