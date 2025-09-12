package com.jasonghent98.fitness_aggregator_api.controller.strava;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaEventWebhookRequest;
import com.jasonghent98.fitness_aggregator_api.service.strava.StravaService;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaActivity;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strava")
public class StravaController {
    private final StravaService stravaService;
    private static final Logger log = LoggerFactory.getLogger(StravaController.class);

    @Value("${strava.webhook.verify-token:}")
    private String verifyToken;

    @Autowired
    public StravaController(StravaService stravaService) {
        this.stravaService = stravaService;
    }

    @GetMapping("/activities")
    public List<StravaActivity> getStravaUser() {
        try {
            return stravaService.getActivitiesForUser();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/stats")
    public List<StravaStats> getAthleteStats() {
        try {
            return stravaService.getStatsForUser();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // WEBHOOKS AND SSE  //

    // 1) Verification handshake (Strava will call this once on subscription create)
    // GET /api/strava/webhook?hub.mode=subscribe&hub.verify_token=XYZ&hub.challenge=abc123
    @GetMapping("/webhook")
    public ResponseEntity<?> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge") String challenge
    ) {
        System.out.println(verifyToken + " FROM StravaController!!!! ");
        if ("subscribe".equalsIgnoreCase(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(Map.of("hub.challenge", challenge));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid verification");
    }

    // 2) Event delivery (Strava posts here for activity/athlete changes)
    @PostMapping("/webhook")
    public ResponseEntity<Void> receive(@RequestBody StravaEventWebhookRequest evt, @RequestHeader Map<String,String> headers) {
        // Minimal logging; keep handler fast (<2s)
        log.info("Strava webhook: object_type={}, aspect_type={}, object_id={}, owner_id={}, updates={}",
                evt.getObject_type(), evt.getAspect_type(), evt.getObject_id(), evt.getOwner_id(), evt.getUpdates());

        // TODO: add signature verification later (X-Strava-Signature) if desired

        try {
            switch (evt.getObject_type()) {
                case "activity" -> {
                    switch (evt.getAspect_type()) {
                        case "create" -> stravaService.onActivityCreate(evt);
                        case "update" -> stravaService.onActivityUpdate(evt);
                        case "delete" -> stravaService.onActivityDelete(evt);
                        default -> log.warn("Unknown aspect_type: {}", evt.getAspect_type());
                    }
                }
                case "athlete" -> {
                    // Typically deauthorization events: updates.authorized = "false"
                    stravaService.onAthleteEvent(evt);
                }
                default -> log.warn("Unknown object_type: {}", evt.getObject_type());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Strava retries on 5xx; only return 200 if you successfully handled/queued the work
            log.error("Failed handling Strava event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}