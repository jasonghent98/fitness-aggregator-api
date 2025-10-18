package com.jasonghent98.fitness_aggregator_api.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;
    private int sessionTtlMinutes = 15;
    private int refreshTtlDays = 60;
    private String issuer;

}