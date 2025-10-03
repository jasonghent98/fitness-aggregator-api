package com.jasonghent98.fitness_aggregator_api.dto.fitbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitbitBodyLog {

    @JsonProperty("weight")
    private List<WeightLog> weight;

    @JsonProperty("fat")
    private List<BodyFatLog> fat;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeightLog {
        @JsonProperty("logId")    private Long logId;
        @JsonProperty("date")     private String date; // "yyyy-MM-dd"
        @JsonProperty("time")     private String time; // "HH:mm:ss"
        @JsonProperty("weight")   private Double weight; // unit = user’s preference
        @JsonProperty("bmi")      private Double bmi;
        @JsonProperty("source")   private String source; // "Aria","API","Mobile"
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BodyFatLog {
        @JsonProperty("logId")    private Long logId;
        @JsonProperty("date")     private String date;
        @JsonProperty("time")     private String time;
        @JsonProperty("fat")      private Double fat; // %
        @JsonProperty("source")   private String source;
    }
}