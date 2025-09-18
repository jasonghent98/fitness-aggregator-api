package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminHrvSummaryPayload {
    private String summaryId;
    private Long userId;
    private String calendarDate;
    private Double averageRmssd;
    private Double averageHeartRate;
    private List<EpochValue> hrvValues; // ✅ detailed time-series HRV samples
}
