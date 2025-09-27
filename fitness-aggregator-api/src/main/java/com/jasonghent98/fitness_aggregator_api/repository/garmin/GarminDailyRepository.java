package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminDailyRepository extends JpaRepository<GarminDailySummary, Long> {

    List<GarminDailySummary> findByActualizeUserIdOrderByCalendarDateDesc(UUID userId);

    List<GarminDailySummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId, LocalDate start, LocalDate end);
}