package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminSleepSummaryPayload {

        @JsonProperty("sleeps")
        private List<SleepSummary> sleepSummaries;

        @Data
        public static class SleepSummary {
                @JsonProperty("userId")
                private String userId;

                @JsonProperty("summaryId")
                private String summaryId;

                @JsonFormat(pattern = "yyyy-MM-dd")
                @JsonProperty("calendarDate")
                private LocalDate calendarDate;

                @JsonProperty("startTimeInSeconds")
                private Long startTimeInSeconds;

                @JsonProperty("startTimeOffsetInSeconds")
                private Integer startTimeOffsetInSeconds;

                @JsonProperty("durationInSeconds")
                private Integer durationInSeconds;

                @JsonProperty("totalNapDurationInSeconds")
                private Integer totalNapDurationInSeconds;

                @JsonProperty("unmeasurableSleepInSeconds")
                private Integer unmeasurableSleepInSeconds;

                @JsonProperty("deepSleepDurationInSeconds")
                private Integer deepSleepDurationInSeconds;

                @JsonProperty("lightSleepDurationInSeconds")
                private Integer lightSleepDurationInSeconds;

                @JsonProperty("remSleepInSeconds")
                private Integer remSleepInSeconds;

                @JsonProperty("awakeDurationInSeconds")
                private Integer awakeDurationInSeconds;

                // Nested maps for stages
                @JsonProperty("sleepLevelsMap")
                private Map<String, List<Map<String, Long>>> sleepLevelsMap;

                @JsonProperty("validation")
                private String validation;

                @JsonProperty("timeOffsetSleepRespiration")
                private Map<String, Double> respirationSamples;

                @JsonProperty("timeOffsetSleepSpo2")
                private Map<String, Integer> spo2Samples;

                @JsonProperty("overallSleepScore")
                private Map<String, Object> overallSleepScore;

                @JsonProperty("sleepScores")
                private Map<String, Object> sleepScores;

                @JsonProperty("naps")
                private List<GarminSleepSummary.NapSummary> naps;

        }
}