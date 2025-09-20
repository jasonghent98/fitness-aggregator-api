package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/** OMIT THIS FOR NOW AS IT TAKES UP A LOT OF SPACE AND IS REDUNDANT ; ADDED FOR CONVENIENCE IF NEEDED LATER */
@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminHealthSummaryPayload {

    @JsonProperty("healthSnapshot")
    private List<HealthSummary> healthSummaries;

    @Data
    public static class HealthSummary {
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("summaryId")
        private String summaryId;

        @JsonProperty("calendarDate")
        private LocalDate calendarDate;

        @JsonProperty("startTimeInSeconds")
        private Long startTimeInSeconds;

        @JsonProperty("durationInSeconds")
        private Integer durationInSeconds;

        @JsonProperty("offsetStartTimeInSeconds")
        private Integer offsetStartTimeInSeconds;

        // The list of metric summaries (heart_rate, respiration, stress, spo2, rmssd_hrv, sdrr_hrv, etc.)
        @JsonProperty("summaries")
        private List<GarminEpochSummaryPayload> summaries;
    }

}