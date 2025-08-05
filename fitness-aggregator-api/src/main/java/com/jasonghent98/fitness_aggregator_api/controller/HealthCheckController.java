package com.jasonghent98.fitness_aggregator_api.controller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    private final DataSource dataSource;

    HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/config")
    public String getHealthCheck() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                return "Database connection OK";
            } else {
                return "Database connection NOT valid";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Database ping failed: " + e.getMessage();
        }
    }

}