package com.jasonghent98.fitness_aggregator_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

// central table for all providers (strava, fitbit, etc..)
@Entity
@Table(
        name = "provider_accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_accounts_provider_ext",
                        columnNames = {"provider_id","provider_user_id"}),
                @UniqueConstraint(name = "uq_provider_accounts_user_provider",
                        columnNames = {"user_id","provider_id"})
        },
        indexes = {
                @Index(name = "idx_provider_accounts_user", columnList = "user_id"),
                @Index(name = "idx_provider_accounts_provider", columnList = "provider_id")
        }
)
@Data
public class ProviderAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // PK
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(nullable=false, name="provider_user_id") private String providerUserId; // string safe to catch all providers

    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;


    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;

    @PrePersist void onCreate() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate void onUpdate() { updatedAt = Instant.now(); }

}