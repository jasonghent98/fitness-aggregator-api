package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/garmin")
public class GarminController {

    private final GarminWebhookService garminWebServ;

    public GarminController(GarminWebhookService garminWebServ) {
        this.garminWebServ = garminWebServ;
    }

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody Map<String, Object> payload,
                                        @RequestHeader Map<String, String> headers) {
        // TODO: verify HMAC signature later using headers (X-...)
        garminWebServ.handleEvent(payload);
        return ResponseEntity.noContent().build(); // 204 quick response
    }
}