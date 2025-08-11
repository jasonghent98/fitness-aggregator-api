package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.util.PkceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/fitbit/auth")
public class FitbitAuthController {
    public final FitbitConfig fitbitConfig;

    /*spring will recognize this is a bean and will handle instantiation and injection*/
    FitbitAuthController(FitbitConfig fitbitConfig) {
        this.fitbitConfig = fitbitConfig;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        String fitbitClientId = fitbitConfig.getClientId();
        String fitbitRedirectURL = fitbitConfig.getRedirectUrl();

        // PKCE
        String verifier = PkceUtil.generateCodeVerifier();
        String challenge = PkceUtil.generateCodeChallenge(verifier);

        String authUrl = UriComponentsBuilder
                .fromHttpUrl("https://www.fitbit.com/oauth2/authorize")
                .queryParam("client_id", fitbitClientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", fitbitRedirectURL)
                .queryParam("scope", String.join(" ", List.of("activity", "heartrate", "sleep")))
                .queryParam("code_challenge", challenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .encode()
                .toUriString();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }
}

