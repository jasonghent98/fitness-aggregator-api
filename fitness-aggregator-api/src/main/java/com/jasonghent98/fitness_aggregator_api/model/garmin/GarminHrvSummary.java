package com.jasonghent98.fitness_aggregator_api.model.garmin;


import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "garmin_hrv_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminHrvSummary {

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

    private Integer lastNightAvg;
    private Integer lastNight5MinHigh;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Double> hrvValues; // store offset→value as JSON
}