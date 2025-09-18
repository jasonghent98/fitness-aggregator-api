package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminRespirationSummaryPayload {
    private String summaryId;
    private Long userId;
    private String calendarDate;
    private Double avgRespirationValue;
    private List<EpochValue> respirationValues;
    private List<EpochValue> sleepRespirationValues;
}