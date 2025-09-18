package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminSleepSummaryPayload {
        private String summaryId;
        private Long userId;
        private String sleepStartTimestampGMT;
        private String sleepEndTimestampGMT;
        private Double durationInSeconds;
        private Integer sleepScore;
        private List<EpochValue> stressDuringSleep;
        private List<EpochValue> respirationDuringSleep;
        private List<EpochValue> pulseOxDuringSleep;
}