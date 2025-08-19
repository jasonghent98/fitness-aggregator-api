package com.jasonghent98.fitness_aggregator_api.model;


import jakarta.persistence.*;
import lombok.Data;

// Provider.java
@Entity
@Table(name = "providers")
@Data
public class Provider {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Short id;
    @Column(nullable=false) private String name; // 'strava | 'garmin'
    @Column(nullable=false, name="auth_type") private String authType; // 'oauth2' | 'oauth1' | 'custom'
    private String authorizeUrl;
    private String tokenUrl;
    private String defaultScopes;
    private String docsUrl;
    @Column(nullable=false) private boolean enabled = true;
}