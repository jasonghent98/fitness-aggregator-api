package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.GarminStressSummaryPayload;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private Map<String, Integer> timeOffsetStressLevelValues;

    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> timeOffsetBodyBatteryValues;

    @Column(columnDefinition = "jsonb")
    private List<GarminStressSummaryPayload.StressSummary.BodyBatteryActivityEvent> bodyBatteryEvents;
}
