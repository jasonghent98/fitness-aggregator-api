package com.jasonghent98.fitness_aggregator_api.model;

import com.jasonghent98.fitness_aggregator_api.enums.UserProviderSyncBackfillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "user_provider_sync_state",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_upss_user_provider_dataset",
                columnNames = {"user_id", "provider_id", "dataset"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProviderSyncState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "provider_id", nullable = false)
    private Short providerId;

    /** e.g. 'sleep','hrv','daily','stress','pulseox','activities','oura_sleep',... */
    @Column(name = "dataset", nullable = false)
    private String dataset;

    @Column(name = "earliest_synced_date")
    private LocalDate earliestSyncedDate;

    @Column(name = "latest_synced_date")
    private LocalDate latestSyncedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "backfill_status", nullable = false)
    @Builder.Default
    private UserProviderSyncBackfillStatus backfillStatus = UserProviderSyncBackfillStatus.IDLE;

    @Column(name = "backfill_started_at")
    private Instant backfillStartedAt;

    @Column(name = "backfill_finished_at")
    private Instant backfillFinishedAt;

    @Column(name = "last_incremental_received_at")
    private Instant lastIncrementalReceivedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        backfillStatus = UserProviderSyncBackfillStatus.IDLE;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}