package com.jasonghent98.fitness_aggregator_api.model.strava;
import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(
        name = "strava_users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_strava_users_user", columnNames = "user_id"),
                @UniqueConstraint(name = "uq_strava_users_athlete", columnNames = "strava_athlete_id")
        }
)
public class StravaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "strava_athlete_id", nullable = false, unique = true)
    private Long stravaAthleteId;

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
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private com.jasonghent98.fitness_aggregator_api.model.User user;

    // getter methods

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public Long getStravaAthleteId() {
        return this.stravaAthleteId;
    }

    // setter methods

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setStravaAthleteId(Long stravaAthleteId) {
        this.stravaAthleteId = stravaAthleteId;
    }

    public void setUser(com.jasonghent98.fitness_aggregator_api.model.User user) {
        this.user = user;
    }


}