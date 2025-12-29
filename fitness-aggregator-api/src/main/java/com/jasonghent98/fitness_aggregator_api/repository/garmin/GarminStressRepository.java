package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminStressSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminStressRepository extends JpaRepository<GarminStressSummary, Long> {

    List<GarminStressSummary> findByActualizeUserIdOrderByCalendarDateDesc(UUID userId);

    List<GarminStressSummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId, LocalDate start, LocalDate end);
}