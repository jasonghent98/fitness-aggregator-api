package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fitbit_hr_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitbitHRSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "actualize_user_id", nullable = false)
    private UUID actualizeUserId;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    private Instant recordedAt;

    private Integer bpm;
    private String zone; // e.g. fat_burn, cardio, peak
}