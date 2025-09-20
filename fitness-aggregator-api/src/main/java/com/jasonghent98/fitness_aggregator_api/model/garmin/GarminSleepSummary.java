package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.config.persistance.converter.StringIntegerMapConverter;
import com.jasonghent98.fitness_aggregator_api.config.persistance.converter.StringObjectMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "garmin_sleep_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminSleepSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summaryId;

    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    @Column(name = "user_id", nullable = false)
    private UUID actualizeUserId;

    private LocalDate calendarDate;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;
    private Integer durationInSeconds;

    private Integer deepSleepDurationInSeconds;
    private Integer lightSleepDurationInSeconds;
    private Integer remSleepInSeconds;
    private Integer awakeDurationInSeconds;

    private Integer totalNapDurationInSeconds;
    private Integer unmeasurableSleepInSeconds;

    private String validation;

    // You can decide later if you want to persist maps (Spo2, scores) into JSON (Postgres JSONB)
    @Convert(converter = StringIntegerMapConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> spo2Samples;

    @Convert(converter = StringObjectMapConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> sleepScores;

    @Convert(converter = StringObjectMapConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> overallSleepScore;
}