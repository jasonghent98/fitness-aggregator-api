package com.jasonghent98.fitness_aggregator_api.repository.strava;

import com.jasonghent98.fitness_aggregator_api.model.strava.StravaStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface StravaStatsRepository extends JpaRepository<StravaStats, UUID> {
    List<StravaStats> findByUserId(UUID userId);
}