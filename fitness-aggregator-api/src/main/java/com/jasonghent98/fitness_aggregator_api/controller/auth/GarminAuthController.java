package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.GarminAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.service.auth.GarminAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/garmin/auth")
public class GarminAuthController {

    private final GarminAuthService garminAuthService;

    public GarminAuthController(
            GarminAuthService garminAuthService
    ) {
        this.garminAuthService = garminAuthService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> startGarminAuth() {
        try {
            String url = garminAuthService.buildGarminAuthorizationUrl();
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", url)
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
            GarminAuthTokenResponse response = garminAuthService.retrieveAndStoreAndReturnToken(state, authCode);
            return ResponseEntity.ok(response); // or redirect to UI page
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}