package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeartRateInsightsService {

    private final GeminiService geminiService;

    public HeartRateInsightsService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate AI insights for heart rate data
     * @param dailiesData List of daily summaries
     * @return AI-generated insight text
     */
    public String generateHeartRateInsights(List<GarminDailySummary> dailiesData) {
        if (dailiesData == null || dailiesData.isEmpty()) {
            return "No heart rate data available to analyze.";
        }

        // Format heart rate data for the prompt
        String formattedData = formatHeartRateData(dailiesData);

        // Create the prompt for Gemini
        String prompt = buildHeartRateInsightPrompt(formattedData, dailiesData.size());

        // Generate insights using Gemini
        return geminiService.generateContent(prompt);
    }

    /**
     * Format heart rate data into a readable string for the AI
     */
    private String formatHeartRateData(List<GarminDailySummary> dailiesData) {
        return dailiesData.stream()
                .map(daily -> {
                    Integer restingHR = daily.getRestingHeartRate();
                    Integer avgHR = daily.getAverageHeartRate();
                    Integer maxHR = daily.getMaxHeartRate();
                    Integer minHR = daily.getMinHeartRate();

                    return String.format(
                            "Date: %s, Resting: %d bpm, Average: %d bpm, Max: %d bpm, Min: %d bpm",
                            daily.getCalendarDate(),
                            restingHR != null ? restingHR : 0,
                            avgHR != null ? avgHR : 0,
                            maxHR != null ? maxHR : 0,
                            minHR != null ? minHR : 0
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Build a comprehensive prompt for Gemini to analyze heart rate data
     */
    private String buildHeartRateInsightPrompt(String formattedData, int dataPoints) {
        return String.format("""
                You are a cardiovascular health analyst. Analyze the following heart rate data from the past %d days and provide a concise, actionable insight in 2-3 sentences.

                Focus on:
                1. Resting heart rate trends (improving, stable, or increasing)
                2. Heart rate variability during activities (max/min range)
                3. One specific, actionable recommendation for cardiovascular health

                Heart Rate Data:
                %s

                Provide a friendly, encouraging insight that helps the user understand their cardiovascular patterns. Keep it under 100 words.
                """, dataPoints, formattedData);
    }
}
