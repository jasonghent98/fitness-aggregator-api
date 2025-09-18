package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminHealthSummaryPayload {
    private String summaryId;
    private Long userId;
    private String startTimeGMT;
    private String endTimeGMT;
    private Integer heartRate;
    private Integer respirationRate;
    private Integer stressLevel;
    private Double bodyBattery;
    // … extend with Garmin’s full spec
    private List<EpochValue> stressDetail;
    private List<EpochValue> respirationDetail;
}