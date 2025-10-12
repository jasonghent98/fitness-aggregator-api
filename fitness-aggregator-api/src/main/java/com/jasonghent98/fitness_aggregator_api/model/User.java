package com.jasonghent98.fitness_aggregator_api.model;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "users")
@Data // handles getters and setters
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String stripeCustomerId;

    @Column(nullable = false, unique = true, columnDefinition = "citext")
    private String email;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    private String dashboardPreset;

    private String trainingStyle;

    private String trainingFocus;

    @Column
    private String subscriptionTier;

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
}