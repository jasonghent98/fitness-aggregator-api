package com.jasonghent98.fitness_aggregator_api.controller.strava;
import com.jasonghent98.fitness_aggregator_api.integrations.strava.StravaService;
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

    @GetMapping("/activies")
    public List<SummaryActivity> getStravaUser() {
        try {
            List<SummaryActivity> summaryActivities = stravaService.getActivities("4dc1390fce5a055bd73c441840eda8964d3e7384");
            return summaryActivities;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}