package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.*;
import org.springframework.core.ParameterizedTypeReference;
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
        return fitbitGet("https://api.fitbit.com/1/user/-/profile.json", accessToken, String.class);
    }

    /** Daily Activity Summary (steps, calories, distance, etc.) */
    public FitbitActivityLog fetchDailyActivity(String accessToken, String userId, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1/user/%s/activities/date/%s.json", userId, date);
        return fitbitGet(url, accessToken, FitbitActivityLog.class);
    }

    /** Sleep Logs */
    public FitbitSleepLog fetchDailySleep(String accessToken, String userId, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1.2/user/%s/sleep/date/%s.json", userId, date);
        return fitbitGet(url, accessToken, FitbitSleepLog.class);
    }

    /** Body Logs */
    public FitbitBodyLog fetchDailyBody(String accessToken, String userId, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1/user/%s/body/log/weight/date/%s.json", userId, date);
        return fitbitGet(url, accessToken, FitbitBodyLog.class);
    }

    /** Food Logs */
    public FitbitFoodLog fetchDailyFood(String accessToken, String userId, LocalDate date) {
        String url = String.format("https://api.fitbit.com/1/user/%s/foods/log/date/%s.json", userId, date);
        return fitbitGet(url, accessToken, FitbitFoodLog.class);
    }

    /** Heart Rate Intraday Time Series (1-day) */
    public String fetchHeartRate(String accessToken, String userId, LocalDate date) {
        String url = String.format(
                "https://api.fitbit.com/1/user/-/activities/heart/date/%s/1d/1min.json", date
        );
        return fitbitGet(url, accessToken, String.class);
    }

    // ---------- Helper ---------- //

    private <T> T fitbitGet(String url, String accessToken, Class<T> typeRef) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        ResponseEntity<T> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                typeRef
        );
        return resp.getBody();
    }
}