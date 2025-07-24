package com.jasonghent98.fitness_aggregator_api.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
