package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminDailySummaryPayload {
    private String summaryId;
    private Long userId;
    private String startTimeGMT;
    private String endTimeGMT;
    private Double durationInSeconds;
    private Double distanceInMeters;
    private Double activeKilocalories;
    private Integer steps;
}
