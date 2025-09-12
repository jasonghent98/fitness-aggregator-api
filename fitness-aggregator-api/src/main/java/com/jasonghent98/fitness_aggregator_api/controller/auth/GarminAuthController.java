package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.strava.StravaConfig;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/garmin/auth")
@RequiredArgsConstructor
public class GarminAuthController {

    private final ProviderAccountRepository proAccRepo;
    private final ProviderRepository proRepo;
    private final ProviderAccountService proAccServ;
    private final FrontendConfig frontendConfig;
    private final JwtService jwtService;

    public GarminAuthController(
            ProviderAccountRepository proAccRepo,
            ProviderRepository proRepo,
            ProviderAccountService proAccServ,
            FrontendConfig frontendConfig,
            JwtService jwtService
    ) {
        this.proAccRepo = proAccRepo;
        this.proRepo = proRepo;
        this.proAccServ = proAccServ;
        this.frontendConfig = frontendConfig;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        String redirectUrl = garminAuthService.getRequestTokenAndRedirectUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(
            @RequestParam("oauth_token") String oauthToken,
            @RequestParam("oauth_verifier") String oauthVerifier
    ) {
        garminAuthService.exchangeForAccessToken(oauthToken, oauthVerifier);
        return ResponseEntity.ok("Garmin account linked");
    }
}