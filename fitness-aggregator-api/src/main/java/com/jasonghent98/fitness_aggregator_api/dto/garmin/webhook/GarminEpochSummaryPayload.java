package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/** OMIT THIS FOR NOW AS IT TAKES UP A LOT OF SPACE AND IS REDUNDANT ; ADDED FOR CONVENIENCE IF NEEDED LATER */
public class GarminEpochSummaryPayload {

    @JsonProperty("summary type")
    private String summaryType; // e.g., heart_rate, respiration, stress, spo2, rmssd_hrv, sdrr_hrv

    @JsonProperty("minValue")
    private Double minValue;

    @JsonProperty("maxValue")
    private Double maxValue;

    @JsonProperty("avgValue")
    private Double avgValue;

    // Time-series samples: offset → value
    @JsonProperty("epochSummaries")
    private Map<String, Double> epochSummaries;

}