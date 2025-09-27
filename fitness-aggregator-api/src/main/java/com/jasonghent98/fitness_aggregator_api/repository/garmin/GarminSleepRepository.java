package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminSleepRepository extends JpaRepository<GarminSleepSummary, Long> {

    List<GarminSleepSummary> findByActualizeUserIdOrderByCalendarDateDesc(UUID userId);

    List<GarminSleepSummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId, LocalDate start, LocalDate end);
}