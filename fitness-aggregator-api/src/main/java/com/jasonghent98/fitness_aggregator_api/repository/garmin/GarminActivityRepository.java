package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminActivitySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GarminActivityRepository extends JpaRepository<GarminActivitySummary, Long> {

    /**
     * Fetch all activities for a given user within a date range (inclusive),
     * ordered by calendarDate ascending.
     */
    List<GarminActivitySummary> findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Fetch all activities for a given user, regardless of date.
     */
    List<GarminActivitySummary> findByActualizeUserId(UUID userId);
}