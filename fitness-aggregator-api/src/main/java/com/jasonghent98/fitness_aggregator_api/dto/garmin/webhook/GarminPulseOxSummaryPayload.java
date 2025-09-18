package com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook;

import lombok.Data;

@Data
public class GarminPulseOxSummaryPayload {
    String summaryId;
    Long userId;
    String calendarDate;
    Double avgSpo2;
    Double avgSpo2Sleep;
    Double avgSpo2Wake;
    List<EpochValue> timeOffsetSpo2Values;
    List<EpochValue> timeOffsetSpo2SleepValues;
    List<EpochValue> timeOffsetSpo2WakeValues;
}