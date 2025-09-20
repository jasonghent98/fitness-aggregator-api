package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminPulseOxSummaryPayload {

    @JsonProperty("pulseox")
    private List<PulseOxSummary> pulseOxSummaries;

    @Data
    public static class PulseOxSummary {
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("summaryId")
        private String summaryId;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("calendarDate")
        private LocalDate calendarDate;

        @JsonProperty("startTimeInSeconds")
        private Long startTimeInSeconds;

        @JsonProperty("durationInSeconds")
        private Integer durationInSeconds;

        @JsonProperty("startTimeOffsetInSeconds")
        private Integer startTimeOffsetInSeconds;

        // Map of time offsets → SpO₂ value (e.g., 93%)
        @JsonProperty("timeOffsetSpo2Values")
        private Map<String, Integer> spo2Values;

        @JsonProperty("onDemand")
        private Boolean onDemand;
    }

}