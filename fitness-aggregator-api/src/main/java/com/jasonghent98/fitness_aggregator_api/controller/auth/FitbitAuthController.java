package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.fitbit.FitbitUserRepository;
import com.jasonghent98.fitness_aggregator_api.util.PkceUtil;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/fitbit/auth")
public class FitbitAuthController {
    public final FitbitConfig fitbitConfig;
    public final FrontendConfig frontendConfig;
    public final FitbitUserRepository fitbitUserRepo;

    /*spring will recognize this is a bean and will handle instantiation and injection*/
    FitbitAuthController(FitbitConfig fitbitConfig, FrontendConfig frontendConfig, FitbitUserRepository fitbitUserRepo) {
        this.frontendConfig = frontendConfig;
        this.fitbitConfig = fitbitConfig;
        this.fitbitUserRepo = fitbitUserRepo;
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
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam Map<String, String> params) {
        try {
            params.forEach((k, v) -> System.out.println(k + ": " + v));
            return ResponseEntity.ok("fitbit callback executed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to exchange token: " + e.getMessage());
        }
    }
}

