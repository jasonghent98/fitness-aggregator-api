package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.GarminStressSummaryPayload;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "garmin_stress_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminStressSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String summaryId;

    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    @Column(name = "user_id", nullable = false)
    private UUID actualizeUserId;

    private LocalDate calendarDate;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;
    private Integer durationInSeconds;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> timeOffsetStressLevelValues;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> timeOffsetBodyBatteryValues;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<GarminStressSummaryPayload.StressSummary.BodyBatteryActivityEvent> bodyBatteryActivityEvents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }
}
