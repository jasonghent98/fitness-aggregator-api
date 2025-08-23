package com.jasonghent98.fitness_aggregator_api.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    private int ttlDays = 28;
    private String issuer = "actualize-api";

    public String getSecret() { return secret; }

    public int getTtlDays() { return ttlDays; }

    public String getIssuer() { return issuer; }
}