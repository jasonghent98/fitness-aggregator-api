package com.jasonghent98.fitness_aggregator_api.repository.ai;

import com.jasonghent98.fitness_aggregator_api.model.ai.AiInsightCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AiInsightCacheRepository extends JpaRepository<AiInsightCache, UUID> {

    /**
     * Find cached insight for a user, metric type, and date
     */
    Optional<AiInsightCache> findByUserIdAndMetricTypeAndCacheDate(
            UUID userId,
            String metricType,
            LocalDate cacheDate
    );

    /**
     * Delete old cache entries (for cleanup job)
     */
    void deleteByUserIdAndMetricTypeAndCacheDateBefore(
            UUID userId,
            String metricType,
            LocalDate beforeDate
    );
}
