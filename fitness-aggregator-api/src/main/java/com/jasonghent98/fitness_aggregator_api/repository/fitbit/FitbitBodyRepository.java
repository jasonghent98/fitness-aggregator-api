package com.jasonghent98.fitness_aggregator_api.repository.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitBodySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FitbitBodyRepository extends JpaRepository<FitbitBodySummary, UUID> {
    List<FitbitBodySummary> findByActualizeUserIdAndLogDateBetween(UUID userId, LocalDate start, LocalDate end);
}