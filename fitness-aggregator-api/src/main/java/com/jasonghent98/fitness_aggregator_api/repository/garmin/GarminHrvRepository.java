package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminHrvSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminHrvRepository extends JpaRepository<GarminHrvSummary, Long> {

    List<GarminHrvSummary> findByActualizeUserIdOrderByCalendarDateDesc(UUID userId);

    List<GarminHrvSummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId, LocalDate start, LocalDate end);
}