package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.config.persistance.converter.StringDoubleMapConverter;
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
@Table(name = "garmin_pulseox_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminPulseOxSummary {

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

    @Convert(converter = StringIntegerMapConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> spo2Samples;
}