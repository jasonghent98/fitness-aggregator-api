package com.jasonghent98.fitness_aggregator_api.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fitbit/auth")
public class FitbitAuthController {
    @GetMapping("/login")
    public String login() {
        return "fitbit login here";

    }

}
