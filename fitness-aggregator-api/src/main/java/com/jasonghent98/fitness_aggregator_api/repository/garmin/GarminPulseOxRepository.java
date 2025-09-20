package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminPulseOxSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GarminPulseOxRepository extends JpaRepository<GarminPulseOxSummary, Long> {

    List<GarminPulseOxSummary> findByUserIdOrderByCalendarDateDesc(String userId);

    List<GarminPulseOxSummary> findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            String userId, LocalDate start, LocalDate end);
}