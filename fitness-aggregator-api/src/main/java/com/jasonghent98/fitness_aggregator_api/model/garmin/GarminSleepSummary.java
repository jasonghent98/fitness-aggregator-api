package com.jasonghent98.fitness_aggregator_api.model.garmin;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private String userId;

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
    @Column(columnDefinition = "jsonb")
    private String spo2Samples;

    @Column(columnDefinition = "jsonb")
    private String sleepScores;

    @Column(columnDefinition = "jsonb")
    private String overallSleepScore;
}