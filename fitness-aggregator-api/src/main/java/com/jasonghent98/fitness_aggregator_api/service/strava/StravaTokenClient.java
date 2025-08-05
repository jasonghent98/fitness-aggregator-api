package com.jasonghent98.fitness_aggregator_api.service.strava;

import com.jasonghent98.fitness_aggregator_api.config.provider.strava.StravaConfig;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class StravaTokenClient {

    private final RestTemplate restTemplate;
    private final StravaConfig stravaConfig;

    public StravaTokenClient(RestTemplate restTemplate, StravaConfig stravaConfig) {
        this.restTemplate = restTemplate;
        this.stravaConfig = stravaConfig;
    }

    public StravaAuthTokenResponse refreshToken(String refreshToken) {
        String tokenUrl = "https://www.strava.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", stravaConfig.getClientId());
        body.add("client_secret", stravaConfig.getClientSecret());
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<StravaAuthTokenResponse> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                StravaAuthTokenResponse.class
        );

        return response.getBody();
    }
}