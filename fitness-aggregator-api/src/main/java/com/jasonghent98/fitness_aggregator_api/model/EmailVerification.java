package com.jasonghent98.fitness_aggregator_api.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "email_verifications",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"),
        indexes = {
                @Index(name = "idx_emails", columnList = "email"),
                @Index(name = "idx_access_token", columnList = "access_token")
        }
)
@Data
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Use citext in Postgres for case-insensitive unique email:
    @Column(nullable = false, unique = true, columnDefinition = "citext")
    private String email;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

}
