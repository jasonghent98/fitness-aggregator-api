package com.jasonghent98.fitness_aggregator_api.model.strava;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "strava_stats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id"})
})

public class StravaStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "biggest_ride_distance")
    private Double biggestRideDistance;

    @Column(name = "biggest_climb_elevation_gain")
    private Double biggestClimbElevationGain;

    // Recent Ride Totals
    @Column(name = "recent_ride_distance")
    private Double recentRideDistance;

    @Column(name = "recent_ride_achievement_count")
    private Integer recentRideAchievementCount;

    @Column(name = "recent_ride_count")
    private Integer recentRideCount;

    @Column(name = "recent_ride_elapsed_time")
    private Integer recentRideElapsedTime;

    @Column(name = "recent_ride_elevation_gain")
    private Double recentRideElevationGain;

    @Column(name = "recent_ride_moving_time")
    private Integer recentRideMovingTime;

    // Expand this class to include ytd_ride_totals, all_ride_totals, etc., as needed.

    // Getters and setters omitted for brevity
}
