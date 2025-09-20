package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GarminDailyRepository extends JpaRepository<GarminDailySummary, Long> {

    List<GarminDailySummary> findByUserIdOrderByCalendarDateDesc(String userId);

    List<GarminDailySummary> findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            String userId, LocalDate start, LocalDate end);
}