package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminStressSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GarminStressRepository extends JpaRepository<GarminStressSummary, Long> {

    List<GarminStressSummary> findByUserIdOrderByCalendarDateDesc(String userId);

    List<GarminStressSummary> findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            String userId, LocalDate start, LocalDate end);
}