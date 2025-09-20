package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.config.persistance.converter.StringIntegerMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

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

    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    @Column(name = "user_id", nullable = false)
    private UUID actualizeUserId;

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

    @Convert(converter = StringIntegerMapConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> heartRateSamples;

    private Integer stressDurations; // could combine average, low, medium, high
}