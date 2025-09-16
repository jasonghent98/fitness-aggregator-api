package com.jasonghent98.fitness_aggregator_api.service.auth;


import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.garmin.GarminConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.garmin.GarminAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import com.jasonghent98.fitness_aggregator_api.util.PkceUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


/*
ProviderAccountRepository proAccRepo,
ProviderRepository proRepo,
ProviderAccountService proAccServ,
FrontendConfig frontendConfig,
JwtService jwtService
*/
@Service
public class GarminAuthService {
    private final GarminConfig garminConfig;
    private final String codeVerifier;

    GarminAuthService(GarminConfig garminConfig) {
        this.garminConfig = garminConfig;
        this.codeVerifier = PkceUtil.generateCodeVerifier();
    }


    public GarminAuthTokenResponse retrieveAndStoreAndReturnToken(String userId, String authCode) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", garminConfig.getClientId());
        params.add("client_secret", garminConfig.getClientSecret());
        params.add("code", authCode); // should've been set from step 1
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", garminConfig.getRedirectUrl());
        params.add("code_verifier", codeVerifier);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<GarminAuthTokenResponse> response = restTemplate.postForEntity(
                garminConfig.getOauthTokenUrl(),
                request,
                GarminAuthTokenResponse.class
        );

        GarminAuthTokenResponse tokenResponse = response.getBody();

        // TODO: Save token data to DB using providerAccountRepo
        System.out.println(tokenResponse + " FROM GARMINAUTHSERVICE!!");

        return tokenResponse;

    }


    public String buildGarminAuthorizationUrl() throws Exception {
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);
        // get userid from thread local
        String userId = UserContext.getUserId().toString();
        System.out.println(userId + " FROM BUILDGARMINAUTHURL!!");

        return garminConfig.getOauthBaseUrl()
                + "?client_id=" + garminConfig.getClientId()
                + "&response_type=code"
                + "&state=" + userId
                + "&redirect_uri=" + URLEncoder.encode(garminConfig.getRedirectUrl(), StandardCharsets.UTF_8)
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256";
    }


}