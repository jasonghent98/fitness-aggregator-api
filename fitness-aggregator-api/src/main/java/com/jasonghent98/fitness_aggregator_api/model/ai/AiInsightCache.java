package com.jasonghent98.fitness_aggregator_api.model.ai;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "ai_insight_cache",
        indexes = {
                @Index(name = "idx_ai_cache_user_metric_date", columnList = "user_id, metric_type, cache_date", unique = true)
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInsightCache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "metric_type", nullable = false, length = 50)
    private String metricType; // e.g., "sleep", "steps", "hrv"

    @Column(name = "cache_date", nullable = false)
    private LocalDate cacheDate;

    @Column(name = "insight", nullable = false, columnDefinition = "TEXT")
    private String insight;

    @Column(name = "data_hash", length = 64)
    private String dataHash; // Optional: hash of input data to detect changes

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}
