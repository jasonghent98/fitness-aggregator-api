package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "fitbit_users",
        uniqueConstraints = {
                @UniqueConstraint(name="uq_fitbit_users_user", columnNames="user_id"),
                @UniqueConstraint(name="uq_fitbit_users_fitbit", columnNames="fitbit_user_id")
        })
public class FitbitUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "fitbit_user_id", nullable = false)
    private String fitbitUserId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column private String scope;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;

    @PrePersist void onCreate() { Instant now = Instant.now(); createdAt = now; updatedAt = now; }
    @PreUpdate  void onUpdate() { updatedAt = Instant.now(); }
}