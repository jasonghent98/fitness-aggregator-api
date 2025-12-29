package com.jasonghent98.fitness_aggregator_api.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
public class FrontendConfig {

    @Value("${frontend.url}")
    private String frontendOrigin;

}