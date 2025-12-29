package com.jasonghent98.fitness_aggregator_api.dto.fitbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/* ===========================
 * HEART RATE (intraday)
 * Endpoint shapes like:
 *  - activities-heart, activities-heart-intraday.dataset[{time,value}]
 * =========================== */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitbitHeartRateLog {

    @JsonProperty("activities-heart")
    private List<DaySummary> activitiesHeart;

    @JsonProperty("activities-heart-intraday")
    private Intraday intraday;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DaySummary {
        @JsonProperty("dateTime") private String date; // "yyyy-MM-dd"
        @JsonProperty("value")    private Value value;

        @Data @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Value {
            @JsonProperty("restingHeartRate") private Integer restingHeartRate;
            @JsonProperty("heartRateZones")   private List<HeartRateZone> zones;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Intraday {
        @JsonProperty("dataset")          private List<Point> dataset;
        @JsonProperty("datasetInterval")  private Integer datasetInterval;
        @JsonProperty("datasetType")      private String datasetType;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        @JsonProperty("time")  private String time;   // "HH:mm:ss"
        @JsonProperty("value") private Integer bpm;   // beats per minute
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeartRateZone {
        private String name;           // "Fat Burn", "Cardio", ...
        private Integer min;
        private Integer max;
        private Integer minutes;
        private Double caloriesOut;
    }
}