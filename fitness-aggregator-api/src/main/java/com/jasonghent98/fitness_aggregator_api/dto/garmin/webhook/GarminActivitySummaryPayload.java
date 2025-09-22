package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GarminActivitySummaryPayload {

    private String userId;
    private String summaryId;
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
}