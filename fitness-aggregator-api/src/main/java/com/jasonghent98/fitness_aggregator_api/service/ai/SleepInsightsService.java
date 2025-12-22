package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SleepInsightsService {

    private final GeminiService geminiService;

    public SleepInsightsService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate AI insights for sleep data
     * @param sleepData List of sleep summaries
     * @return AI-generated insight text
     */
    public String generateSleepInsights(List<GarminSleepSummary> sleepData) {
        if (sleepData == null || sleepData.isEmpty()) {
            return "No sleep data available to analyze.";
        }

        // Format sleep data for the prompt
        String formattedData = formatSleepData(sleepData);

        // Create the prompt for Gemini
        String prompt = buildSleepInsightPrompt(formattedData, sleepData.size());

        // Generate insights using Gemini
        return geminiService.generateContent(prompt);
    }

    /**
     * Format sleep data into a readable string for the AI
     */
    private String formatSleepData(List<GarminSleepSummary> sleepData) {
        return sleepData.stream()
                .map(sleep -> String.format(
                        "Date: %s, Duration: %.1f hours, Deep: %.0f%%, REM: %.0f%%, Light: %.0f%%, Awake: %.0f%%, Score: %d",
                        sleep.getCalendarDate(),
                        sleep.getDurationInSeconds() / 3600.0,
                        sleep.getDeepSleepSeconds() * 100.0 / sleep.getDurationInSeconds(),
                        sleep.getRemSleepSeconds() * 100.0 / sleep.getDurationInSeconds(),
                        sleep.getLightSleepSeconds() * 100.0 / sleep.getDurationInSeconds(),
                        sleep.getAwakeSleepSeconds() * 100.0 / sleep.getDurationInSeconds(),
                        sleep.getSleepScores() != null ? sleep.getSleepScores().getOverallSleepScore() : 0
                ))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Build a comprehensive prompt for Gemini to analyze sleep data
     */
    private String buildSleepInsightPrompt(String formattedData, int dataPoints) {
        return String.format("""
                You are a sleep health analyst. Analyze the following sleep data from the past %d days and provide a concise, actionable insight in 2-3 sentences.

                Focus on:
                1. Overall sleep quality trends (improving, declining, or stable)
                2. Sleep stage distribution (deep, REM, light sleep percentages)
                3. One specific, actionable recommendation

                Sleep Data:
                %s

                Provide a friendly, encouraging insight that helps the user understand their sleep patterns. Keep it under 100 words.
                """, dataPoints, formattedData);
    }
}
