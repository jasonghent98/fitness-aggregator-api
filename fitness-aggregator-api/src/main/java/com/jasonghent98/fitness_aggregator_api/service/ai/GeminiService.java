package com.jasonghent98.fitness_aggregator_api.service.ai;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final String apiKey;
    private final GenerativeModel model;

    public GeminiService(@Value("${GEMINI_API_KEY}") String apiKey) {
        this.apiKey = apiKey;
        this.model = new GenerativeModel.Builder()
                .setModelName("gemini-1.5-flash")
                .setApiKey(apiKey)
                .build();
    }

    /**
     * Generate text content using Gemini API
     * @param prompt The prompt to send to Gemini
     * @return The generated text response
     */
    public String generateContent(String prompt) {
        try {
            GenerateContentResponse response = model.generateContent(prompt);
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate content with Gemini: " + e.getMessage(), e);
        }
    }
}
