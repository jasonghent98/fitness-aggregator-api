package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    // case-insensitive by virtue of citext on the column, but this reads clearer
    Optional<EmailVerification> findByEmail(String email);
    Optional<EmailVerification> findByAccessToken(String accessToken);
    void deleteByEmail(String email);
}