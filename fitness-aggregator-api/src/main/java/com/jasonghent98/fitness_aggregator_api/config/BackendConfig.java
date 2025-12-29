package com.jasonghent98.fitness_aggregator_api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BackendConfig {
    @Value("${backend.url}")
    private String backendOrigin;
}