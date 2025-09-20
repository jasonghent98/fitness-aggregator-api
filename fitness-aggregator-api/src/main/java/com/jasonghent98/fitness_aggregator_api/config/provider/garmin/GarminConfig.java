package com.jasonghent98.fitness_aggregator_api.config.provider.garmin;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class GarminConfig {
    @Value("${garmin.CLIENT_ID}")
    private String clientId;

    @Value("${garmin.CLIENT_SECRET}")
    private String clientSecret;

    @Value("${garmin.REDIRECT_URL}")
    private String redirectUrl;

    @Value("${garmin.OAUTH_BASE_URL}")
    private String oauthBaseUrl;

    @Value("${garmin.OAUTH_TOKEN_URL}")
    private String oauthTokenUrl;
}

