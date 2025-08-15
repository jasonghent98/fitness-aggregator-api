package com.jasonghent98.fitness_aggregator_api.repository.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FitbitUserRepository extends JpaRepository<FitbitUser, UUID> {
    Optional<FitbitUser> findByUserId(UUID userId);
    Optional<FitbitUser> findByFitbitUserId(String fitbitUserId);
}
