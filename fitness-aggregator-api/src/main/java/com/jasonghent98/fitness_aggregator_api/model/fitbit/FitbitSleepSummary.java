package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fitbit_sleep_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitbitSleepSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "actualize_user_id", nullable = false)
    private UUID actualizeUserId;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    private LocalDate sleepDate;

    private Integer durationMinutes;
    private Integer efficiency;
    private Integer deepMinutes;
    private Integer remMinutes;
    private Integer lightMinutes;
    private Integer wakeMinutes;
}