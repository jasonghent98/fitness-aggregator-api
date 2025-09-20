package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminHrvSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GarminHrvRepository extends JpaRepository<GarminHrvSummary, Long> {

    List<GarminHrvSummary> findByUserIdOrderByCalendarDateDesc(String userId);

    List<GarminHrvSummary> findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            String userId, LocalDate start, LocalDate end);
}