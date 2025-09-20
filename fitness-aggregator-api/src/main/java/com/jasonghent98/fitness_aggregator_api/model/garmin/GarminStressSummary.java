package com.jasonghent98.fitness_aggregator_api.model.garmin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "garmin_stress_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminStressSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summaryId;
    private String userId;

    private LocalDate calendarDate;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;
    private Integer durationInSeconds;

    @Column(columnDefinition = "jsonb")
    private String timeOffsetStressLevelValues;

    @Column(columnDefinition = "jsonb")
    private String timeOffsetBodyBatteryValues;

    @Column(columnDefinition = "jsonb")
    private String bodyBatteryEvents;
}
