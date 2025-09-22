package com.jasonghent98.fitness_aggregator_api.repository.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminActivitySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GarminActivityRepository extends JpaRepository<GarminActivitySummary, Long> {

    /**
     * Fetch all activities for a given user within a date range (inclusive),
     * ordered by calendarDate ascending.
     */
    List<GarminActivitySummary> findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Fetch all activities for a given user, regardless of date.
     */
    List<GarminActivitySummary> findByUserId(String userId);
}