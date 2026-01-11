package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminHrvSummary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HrvInsightsService {

    private final GeminiService geminiService;

    public HrvInsightsService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Generate AI insights for HRV data
     * @param hrvData List of HRV summaries
     * @return AI-generated insight text
     */
    public String generateHrvInsights(List<GarminHrvSummary> hrvData) {
        if (hrvData == null || hrvData.isEmpty()) {
            return "No HRV data available to analyze.";
        }

        // Format HRV data for the prompt
        String formattedData = formatHrvData(hrvData);

        // Create the prompt for Gemini
        String prompt = buildHrvInsightPrompt(formattedData, hrvData.size());

        // Generate insights using Gemini
        return geminiService.generateContent(prompt);
    }

    /**
     * Format HRV data into a readable string for the AI
     */
    private String formatHrvData(List<GarminHrvSummary> hrvData) {
        return hrvData.stream()
                .map(hrv -> {
                    Integer lastNightAvg = hrv.getLastNightAvg();
                    Integer lastNight5MinHigh = hrv.getLastNight5MinHigh();

                    return String.format(
                            "Date: %s, Last Night Avg: %d ms, 5-Min High: %d ms",
                            hrv.getCalendarDate(),
                            lastNightAvg != null ? lastNightAvg : 0,
                            lastNight5MinHigh != null ? lastNight5MinHigh : 0
                    );
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Build a comprehensive prompt for Gemini to analyze HRV data
     */
    private String buildHrvInsightPrompt(String formattedData, int dataPoints) {
        return String.format("""
                You are a recovery and stress analyst. Analyze the following Heart Rate Variability (HRV) data from the past %d days and provide a concise, actionable insight in 2-3 sentences.

                Focus on:
                1. Overall HRV trends (improving recovery, declining, or stable)
                2. Recovery status patterns and what they indicate
                3. One specific, actionable recommendation for improving recovery

                HRV Data:
                %s

                Provide a friendly, encouraging insight that helps the user understand their recovery patterns. Keep it under 100 words.
                """, dataPoints, formattedData);
    }
}
