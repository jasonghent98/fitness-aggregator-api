package com.jasonghent98.fitness_aggregator_api.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;          // from env: JWT_SECRET
    private int ttlDays = 28;        // from env: JWT_TTL_DAYS (optional)
    private String issuer = "actualize-api";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public int getTtlDays() { return ttlDays; }
    public void setTtlDays(int ttlDays) { this.ttlDays = ttlDays; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}