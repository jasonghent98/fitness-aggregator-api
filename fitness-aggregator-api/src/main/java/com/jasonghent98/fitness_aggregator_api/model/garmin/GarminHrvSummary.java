package com.jasonghent98.fitness_aggregator_api.model.garmin;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "garmin_hrv_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminHrvSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summaryId;
    private String userId;

    private LocalDate calendarDate;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;
    private Integer durationInSeconds;

    private Double lastNightAvg;
    private Double lastNight5MinHigh;

    @Column(columnDefinition = "jsonb")
    private String hrvValues; // store offset→value as JSON
}