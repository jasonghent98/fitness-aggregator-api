package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminPulseOxSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminPulseOxRepository extends JpaRepository<GarminPulseOxSummary, Long> {

    List<GarminPulseOxSummary> findByActualizeUserIdOrderByCalendarDateDesc(UUID userId);

    List<GarminPulseOxSummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId, LocalDate start, LocalDate end);
}