package com.jasonghent98.fitness_aggregator_api.dto.fitbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitbitFoodLog {

    @JsonProperty("foods")
    private List<FoodEntry> foods;

    @JsonProperty("summary")
    private FoodSummary summary;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FoodEntry {
        @JsonProperty("logId")       private Long logId;
        @JsonProperty("loggedFood")  private LoggedFood loggedFood;
        @JsonProperty("nutritionalValues") private NutritionalValues nutritionalValues;
        @JsonProperty("mealTypeId")  private Integer mealTypeId; // 1=Breakfast, etc.
        @JsonProperty("amount")      private Double amount;
        @JsonProperty("unit")        private Unit unit;
        @JsonProperty("logDate")     private String logDate; // "yyyy-MM-dd"
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoggedFood {
        private String name;
        private String brand;
        private Double calories;
        private Double servingSize;
        private Unit unit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NutritionalValues {
        private Double calories;
        private Double carbs;
        private Double fat;
        private Double fiber;
        private Double protein;
        private Double sodium;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Unit {
        private String id;
        private String name; // "gram", "serving", etc.
        private String type; // "METRIC", "UNIT"
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FoodSummary {
        private Double calories;
        private Double carbs;
        private Double fat;
        private Double fiber;
        private Double protein;
        private Double sodium;
        private Double water; // ml
    }
}