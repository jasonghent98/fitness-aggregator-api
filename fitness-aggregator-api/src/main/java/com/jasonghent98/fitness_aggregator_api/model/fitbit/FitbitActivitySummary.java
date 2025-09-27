package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fitbit_activity_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitbitActivitySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "actualize_user_id", nullable = false)
    private UUID actualizeUserId;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    private LocalDate activityDate;

    private String activityType; // e.g. run, walk
    private Integer durationInMinutes;
    private Double distanceInKm;
    private Integer steps;
    private Integer calories;
}