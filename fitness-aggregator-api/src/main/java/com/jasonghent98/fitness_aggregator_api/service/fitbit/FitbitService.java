package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitAuthTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FitbitService {

    private final FitbitConfig fitbitConfig;
    private final RestTemplate restTemplate;

    public FitbitService(FitbitConfig fitbitConfig, RestTemplate restTemplate) {
        this.fitbitConfig = fitbitConfig;
        this.restTemplate = restTemplate;
    }

    /** Exchange OAuth code for access/refresh tokens */
    public Optional<FitbitAuthTokenResponse> exchangeCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(fitbitConfig.getClientId(), fitbitConfig.getClientSecret());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", fitbitConfig.getClientId());
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", fitbitConfig.getRedirectUrl());

        ResponseEntity<FitbitAuthTokenResponse> resp = restTemplate.postForEntity(
                "https://api.fitbit.com/oauth2/token",
                new HttpEntity<>(form, headers),
                FitbitAuthTokenResponse.class
        );

        return Optional.ofNullable(resp.getBody());
    }

    /** Refresh access token */
    public Optional<FitbitAuthTokenResponse> refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(fitbitConfig.getClientId(), fitbitConfig.getClientSecret());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        ResponseEntity<FitbitAuthTokenResponse> resp = restTemplate.postForEntity(
                "https://api.fitbit.com/oauth2/token",
                new HttpEntity<>(form, headers),
                FitbitAuthTokenResponse.class
        );

        return Optional.ofNullable(resp.getBody());
    }

    // ---------- Fetch Data ---------- //

    /** User Profile */
    public String fetchProfile(String accessToken) {
        return get("https://api.fitbit.com/1/user/-/profile.json", accessToken);
    }

    /** Daily Activity Summary (steps, calories, distance, etc.) */
    public String fetchDailyActivity(String accessToken, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1/user/-/activities/date/%s.json", date);
        return get(url, accessToken);
    }

    /** Sleep Logs */
    public String fetchSleep(String accessToken, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1.2/user/-/sleep/date/%s.json", date);
        return get(url, accessToken);
    }

    /** Heart Rate Intraday Time Series (1-day) */
    public String fetchHeartRate(String accessToken, LocalDate date) {
        String url = String.format(
                "https://api.fitbit.com/1/user/-/activities/heart/date/%s/1d/1min.json", date
        );
        return get(url, accessToken);
    }

    // ---------- Helper ---------- //

    private String get(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        ResponseEntity<String> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        return resp.getBody();
    }
}