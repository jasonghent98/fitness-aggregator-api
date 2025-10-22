package com.jasonghent98.fitness_aggregator_api.controller.fitbit;

import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.service.fitbit.FitbitIngestionService;
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

    private final FitbitConfig fitbitConfig;


    public FitbitWebhookController(FitbitService fitbitService, FitbitIngestionService fitbitIngestionService, FitbitConfig fitbitConfig) {
        this.fitbitService = fitbitService;
        this.fitbitIngestionService = fitbitIngestionService;
        this.fitbitConfig = fitbitConfig;
    }

    // Verification handshake (like Strava/Garmin)
    @GetMapping("/verify")
    public ResponseEntity<Void> verifySubscription(@RequestParam(name="verify") String verify) {
        System.out.println(fitbitConfig.getWebhookVerificationCode() + " from /verify");
        if (!verify.equals(fitbitConfig.getWebhookVerificationCode())) {
            return ResponseEntity.status(404).build();
        }
        // Respond with the same header and 204 No Content
        return ResponseEntity.status(204).build();
    }

    // Verification handshake (like Strava/Garmin)
    @GetMapping("/callback")
    public ResponseEntity<Void> verifyCallback(@RequestParam(name="code") String code) {
        System.out.println(code + " from /api/fitbit/webhook/callback");
        return ResponseEntity.status(204).build();
    }

    // Actual push events
    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody String raw) {
        // Fitbit sends event payloads with collectionType (activities, sleep, etc.)
        // fitbitIngestionService.handleWebhook(raw);
        return ResponseEntity.ok().build();
    }
}