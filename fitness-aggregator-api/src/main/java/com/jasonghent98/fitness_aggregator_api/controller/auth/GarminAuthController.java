package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.service.ProviderRegistryService;
import com.jasonghent98.fitness_aggregator_api.service.auth.GarminAuthService;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminBackfillService;
import com.jasonghent98.fitness_aggregator_api.service.sync.BackfillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/garmin/auth")
public class GarminAuthController {

    private final GarminAuthService garminAuthService;
    private final FrontendConfig frontendConfig;
    private final BackfillService backfillService;
    private final ProviderRegistryService providerRegistryService;
    private final Short GARMIN_PROVIDER_ID = 1;

    public GarminAuthController(
            GarminAuthService garminAuthService,
            FrontendConfig frontendConfig,
            BackfillService backfillService,
            ProviderRegistryService providerRegistryService
    ) {
        this.garminAuthService = garminAuthService;
        this.frontendConfig = frontendConfig;
        this.backfillService = backfillService;
        this.providerRegistryService = providerRegistryService;
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
            // trigger async backfill for user
            LocalDate end   = LocalDate.now(ZoneOffset.UTC).minusDays(1); // yesterday
            LocalDate start = end.minusDays(29);

            AtomicReference<Map<String, Provider>> codeToProviders = providerRegistryService.getCodeToProvidersCache();
            backfillService.triggerBackfill(UserContext.getUserId(), codeToProviders.get().get("garmin"), "FREE");

            // redirect back
            String url = frontendConfig.getFrontendOrigin() + "/app/onboarding/connect?provider=garmin&status=success";
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}