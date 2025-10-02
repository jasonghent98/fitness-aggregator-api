package com.jasonghent98.fitness_aggregator_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();

        // TODO: put your actual frontend origins here (dev + any preview domains you use)
        cfg.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://dev.actualize.fit",
                "https://actualize-dev.vercel.app", // optional: your Vercel preview domain
                "https://test.actualize.fit",
                "https://actualize.fit",
                "https://www.strava.com"
        ));

        cfg.setAllowCredentials(true); // if you use cookies/session
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With","X-Actualize-Session","Accept","Origin","Referer"));
        cfg.setExposedHeaders(List.of("Location")); // optional, if you read Location on 3xx
        cfg.setMaxAge(3600L); // cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(source);
    }
}