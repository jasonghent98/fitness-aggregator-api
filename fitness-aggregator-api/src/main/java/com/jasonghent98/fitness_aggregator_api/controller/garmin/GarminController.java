package com.jasonghent98.fitness_aggregator_api.controller.garmin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/garmin")
public class GarminController {



    public GarminController() {}

    @PostMapping
    public void receive(@RequestBody Map<String, Object> payload,
                                        @RequestHeader Map<String, String> headers) {
        // TODO: verify HMAC signature later using headers (X-...)
    }
}