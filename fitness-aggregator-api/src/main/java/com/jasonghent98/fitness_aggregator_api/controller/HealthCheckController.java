package com.jasonghent98.fitness_aggregator_api.controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/config")
    public void getHealthCheck(){
        /*ping the db server and return an OK*/
    }

}