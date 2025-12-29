package com.jasonghent98.fitness_aggregator_api.dto;
import lombok.Data;
import java.util.Map;

@Data
public class WaitlistRequest {
    private String email;

    // Optional survey payload (key-value, keep flexible)
    private Map<String, Object> survey;

    // Optional campaign/source fields
    private String source;     // e.g., "landing_page", "twitter"

    // GDPR-style consent (optional but recommended)
    private Boolean marketingConsent;
}
