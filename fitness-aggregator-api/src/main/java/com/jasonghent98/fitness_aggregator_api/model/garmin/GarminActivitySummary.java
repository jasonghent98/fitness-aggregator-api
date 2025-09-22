package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.GarminActivitySummaryPayload;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "garmin_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminActivitySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // internal primary key

    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    @Column(name = "user_id", nullable = false)
    private UUID actualizeUserId;

    private String summaryId;
    private LocalDate calendarDate;

    @Column(unique = true)
    private Long activityId;

    private String activityName;
    private String activityDescription;


    private Boolean isParent;

    private String parentSummaryId;

    private Integer durationInSeconds;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;

    private String activityType;

    private Double averageBikeCadenceInRoundsPerMinute;
    private Integer averageHeartRateInBeatsPerMinute;
    private Integer averageRunCadenceInStepsPerMinute;
    private Integer averagePushCadenceInPushesPerMinute;
    private Double averageSpeedInMetersPerSecond;
    private Integer averageSwimCadenceInStrokesPerMinute;
    private Double averagePaceInMinutesPerKilometer;

    private Integer activeKilocalories;
    private String deviceName;
    private Double distanceInMeters;

    private Integer maxBikeCadenceInRoundsPerMinute;
    private Integer maxHeartRateInBeatsPerMinute;
    private Double maxPaceInMinutesPerKilometer;
    private Integer maxRunCadenceInStepsPerMinute;
    private Integer maxPushCadenceInPushesPerMinute;
    private Double maxSpeedInMetersPerSecond;

    private Integer numberOfActiveLengths;
    private Double startingLatitudeInDegree;
    private Double startingLongitudeInDegree;

    private Integer steps;
    private Integer pushes;
    private Double totalElevationGainInMeters;
    private Double totalElevationLossInMeters;

    private Boolean manual;
    private Boolean isWebUpload;

    /*
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<GarminActivitySummaryPayload.ActivityLap> laps;
     */
}