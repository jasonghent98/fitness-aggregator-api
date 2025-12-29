package com.jasonghent98.fitness_aggregator_api.repository.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitActivitySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FitbitActivityRepository extends JpaRepository<FitbitActivitySummary, UUID> {
    List<FitbitActivitySummary> findByActualizeUserIdAndActivityDateBetween(UUID userId, LocalDate start, LocalDate end);
}