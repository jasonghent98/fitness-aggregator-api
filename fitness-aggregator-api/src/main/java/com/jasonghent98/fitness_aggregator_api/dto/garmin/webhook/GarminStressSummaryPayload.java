package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminStressSummaryPayload {
        private String summaryId;
        private Long userId;
        private String calendarDate;
        private Double avgStressLevel;
        private Integer totalStressDurationInSecs;
        private List<EpochValue> stressValues;
}