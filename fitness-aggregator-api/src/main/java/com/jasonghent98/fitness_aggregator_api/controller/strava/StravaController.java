package com.jasonghent98.fitness_aggregator_api.controller.strava;
import com.jasonghent98.fitness_aggregator_api.context.strava.StravaContext;
import com.jasonghent98.fitness_aggregator_api.integrations.strava.StravaService;
import io.swagger.client.model.ActivityStats;
import io.swagger.client.model.SummaryActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<SummaryActivity> getStravaUser() {
        try {
            String userAccessToken = StravaContext.getAccessToken();
            List<SummaryActivity> summaryActivities = stravaService.getActivities(userAccessToken);
            return summaryActivities;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/stats")
    public ActivityStats getAthleteStats() {
        try {
            String userAccessToken = StravaContext.getAccessToken();
            ActivityStats summaryStats = stravaService.getAthleteStats(userAccessToken);
            return summaryStats;
        } catch (Exception e) {
            e.printStackTrace();
            return new ActivityStats();
        }
    }
}