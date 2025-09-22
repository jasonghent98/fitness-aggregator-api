package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminActivitySummaryPayload {


        @JsonProperty("activities")
        private List<ActivitySummary> activitySummary;

        @Data
        public static class ActivitySummary {
            @JsonProperty("userId")
            private String userId;
            @JsonProperty("summaryId")
            private String summaryId;
            @JsonProperty("activityId")
            private Long activityId;

            @JsonProperty("activityName")
            private String activityName;
            @JsonProperty("activityDescription")
            private String activityDescription;
            @JsonProperty("isParent")
            private Boolean isParent;
            @JsonProperty("parentSummaryId")
            private String parentSummaryId;

            @JsonProperty("durationInSeconds")
            private Integer durationInSeconds;
            @JsonProperty("startTimeInSeconds")
            private Long startTimeInSeconds;
            @JsonProperty("startTimeOffsetInSeconds")
            private Integer startTimeOffsetInSeconds;
            @JsonProperty("activityType")
            private String activityType;

            @JsonProperty("averageBikeCadenceInRoundsPerMinute")
            private Double averageBikeCadenceInRoundsPerMinute;
            @JsonProperty("averageHeartRateInBeatsPerMinute")
            private Integer averageHeartRateInBeatsPerMinute;
            @JsonProperty("averageRunCadenceInStepsPerMinute")
            private Integer averageRunCadenceInStepsPerMinute;
            @JsonProperty("averagePushCadenceInPushesPerMinute")
            private Integer averagePushCadenceInPushesPerMinute;
            @JsonProperty("averageSpeedInMetersPerSecond")
            private Double averageSpeedInMetersPerSecond;
            @JsonProperty("averageSwimCadenceInStrokesPerMinute")
            private Integer averageSwimCadenceInStrokesPerMinute;
            @JsonProperty("averagePaceInMinutesPerKilometer")
            private Double averagePaceInMinutesPerKilometer;

            @JsonProperty("activeKilocalories")
            private Integer activeKilocalories;
            @JsonProperty("deviceName")
            private String deviceName;
            @JsonProperty("distanceInMeters")
            private Double distanceInMeters;

            @JsonProperty("maxBikeCadenceInRoundsPerMinute")
            private Integer maxBikeCadenceInRoundsPerMinute;
            @JsonProperty("maxHeartRateInBeatsPerMinute")
            private Integer maxHeartRateInBeatsPerMinute;
            @JsonProperty("maxPaceInMinutesPerKilometer")
            private Double maxPaceInMinutesPerKilometer;
            @JsonProperty("maxRunCadenceInStepsPerMinute")
            private Integer maxRunCadenceInStepsPerMinute;
            @JsonProperty("maxPushCadenceInPushesPerMinute")
            private Integer maxPushCadenceInPushesPerMinute;
            @JsonProperty("maxSpeedInMetersPerSecond")
            private Double maxSpeedInMetersPerSecond;

            @JsonProperty("numberOfActiveLengths")
            private Integer numberOfActiveLengths;
            @JsonProperty("startingLatitudeInDegree")
            private Double startingLatitudeInDegree;
            @JsonProperty("startingLongitudeInDegree")
            private Double startingLongitudeInDegree;

            @JsonProperty("steps")
            private Integer steps;
            @JsonProperty("pushes")
            private Integer pushes;
            @JsonProperty("totalElevationGainInMeters")
            private Double totalElevationGainInMeters;
            @JsonProperty("totalElevationLossInMeters")
            private Double totalElevationLossInMeters;

            @JsonProperty("manual")
            private Boolean manual;
            @JsonProperty("isWebUpload")
            private Boolean isWebUpload;

        }
}