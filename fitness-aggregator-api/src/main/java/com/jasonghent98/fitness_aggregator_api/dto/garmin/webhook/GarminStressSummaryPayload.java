package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
public class GarminStressSummaryPayload {

        @JsonProperty("stressDetails")
        private List<StressSummary> stressSummaries;

        @Data
        public static class StressSummary {
                @JsonProperty("userId")
                private String userId;

                @JsonProperty("summaryId")
                private String summaryId;

                @JsonProperty("startTimeInSeconds")
                private Long startTimeInSeconds;

                @JsonProperty("startTimeOffsetInSeconds")
                private Integer startTimeOffsetInSeconds;

                @JsonProperty("durationInSeconds")
                private Integer durationInSeconds;

                @JsonProperty("calendarDate")
                private LocalDate calendarDate;

                @JsonProperty("timeOffsetStressLevelValues")
                private Map<String, Integer> timeOffsetStressLevelValues;

                @JsonProperty("timeOffsetBodyBatteryValues")
                private Map<String, Integer> timeOffsetBodyBatteryValues;

                // 👇 SINGLE OBJECT
                @JsonProperty("bodyBatteryDynamicFeedbackEvent")
                private BodyBatteryFeedbackEvent bodyBatteryDynamicFeedbackEvent;

                // 👇 LIST of OBJECTS
                @JsonProperty("bodyBatteryActivityEventList")
                private List<BodyBatteryActivityEvent> bodyBatteryActivityEventList;

                @Data
                public static class BodyBatteryFeedbackEvent {
                        @JsonProperty("eventStartTimeInSeconds")
                        private Long eventStartTimeInSeconds;

                        @JsonProperty("bodyBatteryLevel")
                        private String bodyBatteryLevel; // e.g. "MODERATE"
                }

                @Data
                public static class BodyBatteryActivityEvent {
                        @JsonProperty("eventType")
                        private String eventType; // "SLEEP", "RECOVERY", "STRESS", etc.

                        @JsonProperty("eventStartTimeInSeconds")
                        private Long eventStartTimeInSeconds;

                        @JsonProperty("eventStartTimeOffsetInSeconds")
                        private Integer eventStartTimeOffsetInSeconds;

                        @JsonProperty("duration")
                        private Integer duration;

                        @JsonProperty("bodyBatteryImpact")
                        private Integer bodyBatteryImpact;
                }
        }
}