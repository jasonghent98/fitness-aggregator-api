package com.jasonghent98.fitness_aggregator_api.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/strava")
public class StravaAuthController {

    @GetMapping("/callback")
    public ResponseEntity<String> handleStravaCallback(@RequestParam("code") String code) {
        // 1. Exchange code for tokens using Strava API
        // 2. Save access_token and refresh_token to your DB
        // 3. Associate them with your app’s user (via session, JWT, etc.)
        return ResponseEntity.ok("Strava account linked!");
    }

}
