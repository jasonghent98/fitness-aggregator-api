package com.jasonghent98.fitness_aggregator_api.service.auth;


import org.springframework.stereotype.Service;

@Service
public class GarminAuthService {
    public String getRequestTokenAndRedirectUrl() {
        // TODO: call Garmin API for request token and build redirect URL
        return "https://connect.garmin.com/oauthConfirm?...";
    }

    public void exchangeForAccessToken(String token, String verifier) {
        // TODO: exchange request token + verifier for access token + secret
    }
}