package com.jasonghent98.fitness_aggregator_api.repository.strava;

import com.jasonghent98.fitness_aggregator_api.model.strava.StravaActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StravaActivityRepository extends JpaRepository<StravaActivity, UUID> {

    // Get all existing Strava activity IDs for a user (for deduplication before inserts)
    @Query("SELECT sa.stravaActivityId FROM StravaActivity sa WHERE sa.userId = :userId")
    List<Long> findAllStravaActivityIdsByUserId(UUID userId);

    // Get recent activities for a user, sorted by start date
    List<StravaActivity> findByUserIdOrderByStartDateDesc(UUID userId);

    // Optional: find one by strava activity id
    StravaActivity findByStravaActivityId(Long stravaActivityId);
}