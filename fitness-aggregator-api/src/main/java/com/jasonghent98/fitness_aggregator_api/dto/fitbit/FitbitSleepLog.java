package com.jasonghent98.fitness_aggregator_api.dto.fitbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.GarminSleepSummaryPayload;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitbitSleepLog {

    @JsonProperty("sleep")
    private List<SleepSession> sleep;

    @JsonProperty("summary")
    private GarminSleepSummaryPayload.SleepSummary summary;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SleepSession {
        @JsonProperty("logId")         private Long logId;
        @JsonProperty("dateOfSleep")   private String dateOfSleep; // "yyyy-MM-dd"
        @JsonProperty("startTime")     private String startTime;   // ISO string
        @JsonProperty("endTime")       private String endTime;     // ISO string
        @JsonProperty("duration")      private Long durationMs;
        @JsonProperty("efficiency")    private Integer efficiency;
        @JsonProperty("isMainSleep")   private Boolean isMainSleep;
        @JsonProperty("levels")        private Levels levels;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Levels {
        @JsonProperty("data")    private List<StagePoint> data;     // classic
        @JsonProperty("shortData") private List<StagePoint> shortData; // naps/brief
        @JsonProperty("summary") private StageSummary summary;      // by stage totals
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StagePoint {
        @JsonProperty("dateTime") private String dateTime; // ISO
        @JsonProperty("level")    private String level;    // "wake","light","deep","rem"
        @JsonProperty("seconds")  private Integer seconds;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StageSummary {
        private StageTotal deep;
        private StageTotal light;
        private StageTotal rem;
        private StageTotal wake;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StageTotal {
        private Integer count;
        private Integer minutes;
        private Integer thirtyDayAvgMinutes;
    }
}
