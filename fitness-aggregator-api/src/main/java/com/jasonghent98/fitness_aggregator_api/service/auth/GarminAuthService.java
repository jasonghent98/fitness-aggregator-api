package com.jasonghent98.fitness_aggregator_api.service.auth;


import com.jasonghent98.fitness_aggregator_api.config.provider.garmin.GarminConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.GarminAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.dto.garmin.GarminAPIUserId;

import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import com.jasonghent98.fitness_aggregator_api.util.PkceUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;


@Service
public class GarminAuthService {
    private final GarminConfig garminConfig;
    private final String codeVerifier;
    private final ProviderAccountService providerAccServ;

    GarminAuthService(GarminConfig garminConfig, ProviderAccountService providerAccServ) {
        this.garminConfig = garminConfig;
        this.codeVerifier = PkceUtil.generateCodeVerifier();
        this.providerAccServ = providerAccServ;
    }


    public GarminAuthTokenResponse retrieveAndStoreAndReturnToken(String userId, String authCode) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // build + make the oauth POST request to get authorization response from garmin
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

        // unpack token response vars
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        String jti = tokenResponse.getJti();
        Instant expiresIn = Instant.now().plusSeconds(tokenResponse.getExpiresIn());


        // ** separate garmin API call required to get provider_user_id **
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(accessToken);
        HttpEntity<Void> request2 = new HttpEntity<>(headers2);

        ResponseEntity<GarminAPIUserId> response2 = restTemplate.exchange(
                "https://apis.garmin.com/wellness-api/rest/user/id",
                HttpMethod.GET,
                request2,
                new ParameterizedTypeReference<>() {}
        );

        String garminUserId = response2.getBody().getUserId();

        // if any data missing, throw exception
        UUID user;
        if (userId == null) {
            throw new Exception("User id not found in garmin auth callback service");
        }
        user = UUID.fromString(userId);

        if (garminUserId == null) {
            throw new Exception("Garmin user id not provided in garmin auth callback service");
        }

        providerAccServ.upsertProviderAccount(
                user,
                "garmin",
                garminUserId,
                accessToken,
                refreshToken,
                expiresIn
        );

        return tokenResponse;

    }


    public String buildGarminAuthorizationUrl() throws Exception {
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);
        // get userid from thread local
        String userId = UserContext.getUserId().toString();

        return garminConfig.getOauthBaseUrl()
                + "?client_id=" + garminConfig.getClientId()
                + "&response_type=code"
                + "&state=" + userId
                + "&redirect_uri=" + URLEncoder.encode(garminConfig.getRedirectUrl(), StandardCharsets.UTF_8)
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256";
    }


}