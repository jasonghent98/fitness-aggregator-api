package com.jasonghent98.fitness_aggregator_api.config;

import com.jasonghent98.fitness_aggregator_api.security.JwtSessionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@Profile("dev")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtSessionFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // keep routes open during dev; your controllers decide what to return
                        .requestMatchers(
                                "/api/strava/auth/**",
                                "/api/fitbit/**",
                                "/api/garmin/**",
                                "/api/oura/**",
                                "/api/auth/whoami",
                                "/api/config",
                                "/actuator/**",
                                "/api/auth/magic/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                )
                // IMPORTANT: make sure your filter actually runs
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(hb -> hb.disable())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        /*cfg.setAllowedOrigins(List.of(
                "https://dev.actualize.fit",
                "http://localhost:3000",
                "https://www.strava.com"
        ));*/
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Content-Type","Authorization","Accept","Origin"));
        /*cfg.setAllowCredentials(true);*/
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}