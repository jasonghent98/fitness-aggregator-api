package com.jasonghent98.fitness_aggregator_api.model;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String fullName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // handled before just before writes are persisted to DB vis JPA API
    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Relationships
    // Only exists in Java code to make it easier to navigate from User → StravaUser. Does NOT create a column
    // The "user" field in the StravaUser class is managing the relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StravaUser stravaUser;

    // Getters and setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}