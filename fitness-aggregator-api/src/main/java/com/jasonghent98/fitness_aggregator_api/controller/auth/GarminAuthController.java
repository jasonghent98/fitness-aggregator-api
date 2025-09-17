package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.service.auth.GarminAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/garmin/auth")
public class GarminAuthController {

    private final GarminAuthService garminAuthService;
    private final FrontendConfig frontendConfig;

    public GarminAuthController(
            GarminAuthService garminAuthService,
            FrontendConfig frontendConfig
    ) {
        this.garminAuthService = garminAuthService;
        this.frontendConfig = frontendConfig;
    }

    @GetMapping("/login")
    public ResponseEntity<?> startGarminAuth() {
        try {
            String url = garminAuthService.buildGarminAuthorizationUrl();
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to build Garmin authorization URL " + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleGarminCallback(
            @RequestParam("code") String authCode,
            @RequestParam("state") String state // userId passed in from /login
    ) {
        try {
            garminAuthService.retrieveAndStoreAndReturnToken(state, authCode);
            String url = frontendConfig.getFrontendOrigin() + "/onboarding/connect?provider=garmin&status=success";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}