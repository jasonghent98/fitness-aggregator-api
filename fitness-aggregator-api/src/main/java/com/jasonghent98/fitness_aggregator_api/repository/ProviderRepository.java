package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// ProviderRepository.java
public interface ProviderRepository extends JpaRepository<Provider, Short> {
    Optional<Provider> findByName(String name); // e.g., "strava"
}