package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
                .map(sleep -> {
                    Integer duration = sleep.getDurationInSeconds();
                    Integer deepSleep = sleep.getDeepSleepDurationInSeconds();
                    Integer remSleep = sleep.getRemSleepInSeconds();
                    Integer lightSleep = sleep.getLightSleepDurationInSeconds();
                    Integer awake = sleep.getAwakeDurationInSeconds();

                    // Calculate percentages safely
                    double deepPercent = (duration != null && duration > 0 && deepSleep != null)
                            ? (deepSleep * 100.0 / duration) : 0;
                    double remPercent = (duration != null && duration > 0 && remSleep != null)
                            ? (remSleep * 100.0 / duration) : 0;
                    double lightPercent = (duration != null && duration > 0 && lightSleep != null)
                            ? (lightSleep * 100.0 / duration) : 0;
                    double awakePercent = (duration != null && duration > 0 && awake != null)
                            ? (awake * 100.0 / duration) : 0;

                    // Extract overall sleep score
                    Integer overallScore = extractOverallScore(sleep);

                    return String.format(
                            "Date: %s, Duration: %.1f hours, Deep: %.0f%%, REM: %.0f%%, Light: %.0f%%, Awake: %.0f%%, Score: %d",
                            sleep.getCalendarDate(),
                            duration != null ? duration / 3600.0 : 0,
                            deepPercent,
                            remPercent,
                            lightPercent,
                            awakePercent,
                            overallScore
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Extract overall sleep score from the sleep scores map
     */
    private Integer extractOverallScore(GarminSleepSummary sleep) {
        // Try sleepScores first
        if (sleep.getSleepScores() != null && sleep.getSleepScores().containsKey("overallSleepScore")) {
            Object score = sleep.getSleepScores().get("overallSleepScore");
            if (score instanceof Integer) {
                return (Integer) score;
            }
        }

        // Try overallSleepScore map
        if (sleep.getOverallSleepScore() != null && sleep.getOverallSleepScore().containsKey("value")) {
            Object score = sleep.getOverallSleepScore().get("value");
            if (score instanceof Integer) {
                return (Integer) score;
            }
        }

        return 0;
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
