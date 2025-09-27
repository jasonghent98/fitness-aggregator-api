package com.jasonghent98.fitness_aggregator_api.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;
    private int ttlMinutes = 15;
    private String issuer;

}