package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class GarminDailySummaryPayload {

    @JsonProperty("dailies")
    private List<DailySummary> dailySummaries;

    @Data
    public static class DailySummary {
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("summaryId")
        private String summaryId;

        @JsonProperty("calendarDate")
        private LocalDate calendarDate;

        // Energy
        @JsonProperty("activeKilocalories")
        private Integer activeKilocalories;

        @JsonProperty("bmrKilocalories")
        private Integer bmrKilocalories;

        // Activity
        @JsonProperty("steps")
        private Integer steps;

        @JsonProperty("distanceInMeters")
        private Double distanceInMeters;

        @JsonProperty("durationInSeconds")
        private Integer durationInSeconds;

        @JsonProperty("activeTimeInSeconds")
        private Integer activeTimeInSeconds;

        @JsonProperty("stepsGoal")
        private Integer stepsGoal;

        // Heart Rate
        @JsonProperty("minHeartRateInBeatsPerMinute")
        private Integer minHeartRate;

        @JsonProperty("averageHeartRateInBeatsPerMinute")
        private Integer averageHeartRate;

        @JsonProperty("maxHeartRateInBeatsPerMinute")
        private Integer maxHeartRate;

        @JsonProperty("restingHeartRateInBeatsPerMinute")
        private Integer restingHeartRate;

        @JsonProperty("timeOffsetHeartRateSamples")
        private Map<String, Integer> heartRateSamples; // timeOffset → bpm

        // Stress
        @JsonProperty("averageStressLevel")
        private Integer averageStressLevel;

        @JsonProperty("maxStressLevel")
        private Integer maxStressLevel;

        @JsonProperty("stressDurationInSeconds")
        private Integer stressDuration;

        @JsonProperty("lowStressDurationInSeconds")
        private Integer lowStressDuration;

        @JsonProperty("mediumStressDurationInSeconds")
        private Integer mediumStressDuration;

        @JsonProperty("highStressDurationInSeconds")
        private Integer highStressDuration;

        // Optional: Intensity/floors
        @JsonProperty("floorsClimbed")
        private Integer floorsClimbed;

        @JsonProperty("moderateIntensityDurationInSeconds")
        private Integer moderateIntensityDuration;

        @JsonProperty("vigorousIntensityDurationInSeconds")
        private Integer vigorousIntensityDuration;

    }

}