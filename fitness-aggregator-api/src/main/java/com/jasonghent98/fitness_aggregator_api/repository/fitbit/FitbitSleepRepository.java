package com.jasonghent98.fitness_aggregator_api.repository.fitbit;

import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitSleepSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FitbitSleepRepository extends JpaRepository<FitbitSleepSummary, UUID> {
    List<FitbitSleepSummary> findByActualizeUserIdAndDateOfSleepBetween(UUID userId, LocalDate start, LocalDate end);
}