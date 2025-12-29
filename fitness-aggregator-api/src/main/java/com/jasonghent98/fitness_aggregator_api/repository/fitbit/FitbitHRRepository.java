package com.jasonghent98.fitness_aggregator_api.repository.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitHRSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FitbitHRRepository extends JpaRepository<FitbitHRSummary, UUID> {
    List<FitbitHRSummary> findByActualizeUserIdAndRecordedAtBetween(UUID userId, Instant start, Instant end);
}