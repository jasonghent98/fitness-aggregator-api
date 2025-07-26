package com.jasonghent98.fitness_aggregator_api.controller;
import com.jasonghent98.fitness_aggregator_api.integrations.strava.StravaService;
import io.swagger.client.model.SummaryActivity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "Backend running!";
    }
    @GetMapping("/db")
    public String checkDatabase() {
        return "DB is running!";
    }
    @GetMapping("/strava")
    public void getStravaUser() {
        List<SummaryActivity> summaryActivities = StravaService.getActivities("5fb9d18611c56d19015e8249e1f5980370506ecc");
        System.out.println(summaryActivities);
    }
}
