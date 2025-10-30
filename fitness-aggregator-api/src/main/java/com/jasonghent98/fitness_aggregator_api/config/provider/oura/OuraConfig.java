package com.jasonghent98.fitness_aggregator_api.config.provider.oura;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OuraConfig {

    @Value("${provider.oura.client-id}")
    private String clientId;

    @Value("${provider.oura.client-secret}")
    private String clientSecret;

    @Value("${provider.oura.redirect-url}")
    private String redirectUrl;

    // e.g. https://cloud.ouraring.com/oauth/authorize  (set in application.yml)
    @Value("${provider.oura.authorize-url}")
    private String authorizeUrl;

    // e.g. https://api.ouraring.com/oauth/token (not used here, but handy later)
    @Value("${provider.oura.token-url:}")
    private String tokenUrl;

    // space-separated scopes, e.g. "daily heartrate workout tag email"
    @Value("${provider.oura.scopes}")
    private String scopes;

    public String getClientId() { return clientId; }
    public String getClientSecret() { return clientSecret; }
    public String getRedirectUrl() { return redirectUrl; }
    public String getAuthorizeUrl() { return authorizeUrl; }
    public String getTokenUrl() { return tokenUrl; }
    public String getScopes() { return scopes; }
}