package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminHrvSummaryPayload {

    @JsonProperty("hrv")
    private List<HrvSummary> hrvSummaries;

    @Data
    public static class HrvSummary {
        @JsonProperty("userId")
        private String userId;

        @JsonProperty("calendarDate")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate calendarDate;

        @JsonProperty("startTimeInSeconds")
        private Long startTimeInSeconds;

        @JsonProperty("durationInSeconds")
        private Integer durationInSeconds;

        @JsonProperty("startTimeOffsetInSeconds")
        private Integer startTimeOffsetInSeconds;

        @JsonProperty("lastNightAvg")
        private Double lastNightAvg;

        @JsonProperty("lastNight5MinHigh")
        private Double lastNight5MinHigh;

        @JsonProperty("hrvValues")
        private Map<String, Double> hrvValues;  // keys are offset in seconds, values are HRV values

    }

}
