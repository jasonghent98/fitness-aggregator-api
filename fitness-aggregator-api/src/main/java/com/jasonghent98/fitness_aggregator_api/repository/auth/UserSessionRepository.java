package com.jasonghent98.fitness_aggregator_api.repository.auth;

import com.jasonghent98.fitness_aggregator_api.model.auth.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    // Find a valid session by refresh token
    Optional<UserSession> findByRefreshTokenAndRevokedAtIsNull(String refreshToken);

    // Find a specific session instance for a user
    Optional<UserSession> findByUserIdAndRefreshToken(UUID userId, String refreshToken);

    // Find all active sessions for a user
    List<UserSession> findByUserIdAndRevokedAtIsNull(UUID userId);

    // Delete expired sessions
    void deleteByRefreshTokenExpiresAtBefore(Instant cutoff);

    // Check if a token exists (useful for validation)
    boolean existsByRefreshToken(String refreshToken);
}