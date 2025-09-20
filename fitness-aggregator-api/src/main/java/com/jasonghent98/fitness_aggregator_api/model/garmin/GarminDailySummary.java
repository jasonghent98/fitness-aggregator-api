package com.jasonghent98.fitness_aggregator_api.model.garmin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "garmin_daily_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminDailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summaryId;
    private String userId;
    private LocalDate calendarDate;

    private Integer steps;
    private Integer stepsGoal;
    private Double distanceInMeters;

    private Integer activeKilocalories;
    private Integer bmrKilocalories;

    private Integer minHeartRate;
    private Integer averageHeartRate;
    private Integer maxHeartRate;
    private Integer restingHeartRate;

    @Column(columnDefinition = "jsonb")
    private String heartRateSamples;

    @Column(columnDefinition = "jsonb")
    private String stressDurations; // could combine average, low, medium, high
}