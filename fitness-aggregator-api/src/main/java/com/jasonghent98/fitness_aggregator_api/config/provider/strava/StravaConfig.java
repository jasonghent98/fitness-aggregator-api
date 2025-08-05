package com.jasonghent98.fitness_aggregator_api.config.provider.strava;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StravaConfig {

    @Value("${strava.CLIENT_ID}")
    private String clientId;

    @Value("${strava.CLIENT_SECRET}")
    private String clientSecret;

    @Value("${strava.REDIRECT_URL}")
    private String redirectUri;

    // Getters
    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUri;
    }
}