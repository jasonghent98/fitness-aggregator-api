package com.jasonghent98.fitness_aggregator_api.controller.strava;
import com.jasonghent98.fitness_aggregator_api.integrations.strava.StravaService;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaActivity;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/strava")
public class StravaController {
    private final StravaService stravaService;

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

    /**
     Receives a list of strava object IDs via webhook (similar to SSE but server to server) ->
        use the returned object ids in the GET webhook controller
     */
    @PostMapping("/webhook")
    public void receiveStravaEvents(RequestBody stravaEvents) {

    }

    /**
     Gets the list of strava event objects by their objectIds

     */
    @GetMapping("/webhook")
    public void getStravaEvents(Integer[] objectIds) {

    }
}