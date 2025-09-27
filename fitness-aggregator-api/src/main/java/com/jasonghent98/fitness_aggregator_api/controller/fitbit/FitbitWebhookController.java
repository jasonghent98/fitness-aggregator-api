package com.jasonghent98.fitness_aggregator_api.controller.fitbit;

import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.service.fitbit.FitbitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fitbit/webhook")
public class FitbitWebhookController {

    private final FitbitService fitbitService;
    private final FitbitIngestionService fitbitIngestionService;

    public FitbitWebhookController(FitbitService fitbitService, FitbitIngestionService fitbitIngestionService) {
        this.fitbitService = fitbitService;
        this.fitbitIngestionService = fitbitIngestionService;
    }

    // Verification handshake (like Strava/Garmin)
    @GetMapping
    public ResponseEntity<String> verify(@RequestParam("verify") String verify) {
        // Fitbit sends a verify param, echo it back
        return ResponseEntity.ok(verify);
    }

    // Actual push events
    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody String raw) {
        // Fitbit sends event payloads with collectionType (activities, sleep, etc.)
        fitbitIngestionService.handleWebhook(raw);
        return ResponseEntity.ok().build();
    }
}