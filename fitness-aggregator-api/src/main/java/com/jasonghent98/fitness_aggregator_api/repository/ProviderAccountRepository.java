package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, UUID> {
    Optional<ProviderAccount> findByProviderAndProviderUserId(Short providerId, String providerUserId);
    Optional<ProviderAccount> findAllByUserId(UUID userId);
}
