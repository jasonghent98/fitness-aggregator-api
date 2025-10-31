package com.jasonghent98.fitness_aggregator_api.config.provider.oura;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class OuraConfig {

    @Value("${oura.CLIENT_ID}")
    private String clientId;

    @Value("${oura.CLIENT_SECRET}")
    private String clientSecret;

    @Value("${oura.REDIRECT_URL}")
    private String redirectUrl;

    // e.g. https://cloud.ouraring.com/oauth/authorize  (set in application.yml)
    @Value("${oura.AUTHORIZE_URL}")
    private String authorizeUrl;

    // e.g. https://api.ouraring.com/oauth/token (not used here, but handy later)
    @Value("${oura.TOKEN_URL}")
    private String tokenUrl;
}