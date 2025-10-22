package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class GarminMapper {

    GarminMapper() {}

    public GarminActivitySummary mapActivityPayload(GarminActivitySummaryPayload dto, UUID actualizeUserId) {
        GarminActivitySummary model = new GarminActivitySummary();
        model.setUserId(dto.getActivitySummary().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setActivityId(dto.getActivitySummary().getFirst().getActivityId());
        model.setSummaryId(dto.getActivitySummary().getFirst().getSummaryId());
        model.setActivityName(dto.getActivitySummary().getFirst().getActivityName());
        model.setActivityType(dto.getActivitySummary().getFirst().getActivityType());
        model.setActivityDescription(dto.getActivitySummary().getFirst().getActivityDescription());
        model.setDurationInSeconds(dto.getActivitySummary().getFirst().getDurationInSeconds());
        model.setStartTimeInSeconds(dto.getActivitySummary().getFirst().getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getActivitySummary().getFirst().getStartTimeOffsetInSeconds());
        model.setAverageBikeCadenceInRoundsPerMinute(dto.getActivitySummary().getFirst().getAverageBikeCadenceInRoundsPerMinute());
        model.setAverageHeartRateInBeatsPerMinute(dto.getActivitySummary().getFirst().getAverageHeartRateInBeatsPerMinute());
        model.setAverageRunCadenceInStepsPerMinute(dto.getActivitySummary().getFirst().getAverageRunCadenceInStepsPerMinute());
        model.setAveragePushCadenceInPushesPerMinute(dto.getActivitySummary().getFirst().getAveragePushCadenceInPushesPerMinute());
        model.setAverageSpeedInMetersPerSecond(dto.getActivitySummary().getFirst().getAverageSpeedInMetersPerSecond());
        model.setActivityType(dto.getActivitySummary().getFirst().getActivityType());
        model.setAverageSwimCadenceInStrokesPerMinute(dto.getActivitySummary().getFirst().getAverageSwimCadenceInStrokesPerMinute());
        model.setAveragePaceInMinutesPerKilometer(dto.getActivitySummary().getFirst().getAveragePaceInMinutesPerKilometer());
        model.setActiveKilocalories(dto.getActivitySummary().getFirst().getActiveKilocalories());
        model.setDistanceInMeters(dto.getActivitySummary().getFirst().getDistanceInMeters());
        model.setDeviceName(dto.getActivitySummary().getFirst().getDeviceName());
        model.setMaxBikeCadenceInRoundsPerMinute(dto.getActivitySummary().getFirst().getMaxBikeCadenceInRoundsPerMinute());
        model.setMaxHeartRateInBeatsPerMinute(dto.getActivitySummary().getFirst().getMaxHeartRateInBeatsPerMinute());
        model.setMaxPaceInMinutesPerKilometer(dto.getActivitySummary().getFirst().getMaxPaceInMinutesPerKilometer());
        model.setMaxRunCadenceInStepsPerMinute(dto.getActivitySummary().getFirst().getMaxRunCadenceInStepsPerMinute());
        model.setMaxPushCadenceInPushesPerMinute(dto.getActivitySummary().getFirst().getMaxPushCadenceInPushesPerMinute());
        model.setMaxSpeedInMetersPerSecond(dto.getActivitySummary().getFirst().getMaxSpeedInMetersPerSecond());
        model.setStartingLatitudeInDegree(dto.getActivitySummary().getFirst().getStartingLatitudeInDegree());
        model.setStartingLongitudeInDegree(dto.getActivitySummary().getFirst().getStartingLongitudeInDegree());
        model.setSteps(dto.getActivitySummary().getFirst().getSteps());
        model.setPushes(dto.getActivitySummary().getFirst().getPushes());
        model.setTotalElevationGainInMeters(dto.getActivitySummary().getFirst().getTotalElevationGainInMeters());
        model.setTotalElevationLossInMeters(dto.getActivitySummary().getFirst().getTotalElevationLossInMeters());
        model.setManual(dto.getActivitySummary().getFirst().getManual());
        model.setIsWebUpload(dto.getActivitySummary().getFirst().getIsWebUpload());

        // model.setLaps(dto.getActivitySummary().getFirst().getLap);
        // persist the utc timestamp to Local Date type for easier querying

        Instant instant = Instant.ofEpochSecond(dto.getActivitySummary().getFirst().getStartTimeInSeconds());
        LocalDate calendarDate = instant.atZone(ZoneOffset.UTC).toLocalDate();
        model.setCalendarDate(calendarDate);
        return model;
    }


    public GarminSleepSummary mapSleepPayload(GarminSleepSummaryPayload dto, UUID actualizeUserId) {
        GarminSleepSummary model = new GarminSleepSummary();
        model.setUserId(dto.getSleepSummaries().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setSummaryId(dto.getSleepSummaries().getFirst().getSummaryId());
        model.setCalendarDate(dto.getSleepSummaries().getFirst().getCalendarDate());
        model.setDurationInSeconds(dto.getSleepSummaries().getFirst().getDurationInSeconds());
        model.setDeepSleepDurationInSeconds(dto.getSleepSummaries().getFirst().getDeepSleepDurationInSeconds());
        model.setLightSleepDurationInSeconds(dto.getSleepSummaries().getFirst().getLightSleepDurationInSeconds());
        model.setRemSleepInSeconds(dto.getSleepSummaries().getFirst().getRemSleepInSeconds());
        model.setAwakeDurationInSeconds(dto.getSleepSummaries().getFirst().getAwakeDurationInSeconds());
        model.setNaps(dto.getSleepSummaries().getFirst().getNaps());
        model.setStartTimeInSeconds(dto.getSleepSummaries().getFirst().getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getSleepSummaries().getFirst().getStartTimeOffsetInSeconds());
        model.setOverallSleepScore(dto.getSleepSummaries().getFirst().getOverallSleepScore());
        model.setSleepScores(dto.getSleepSummaries().getFirst().getSleepScores());
        return model;
    }

    public GarminHrvSummary mapHrvPayload(GarminHrvSummaryPayload dto, UUID actualizeUserId) {
        GarminHrvSummary model = new GarminHrvSummary();
        model.setSummaryId(dto.getHrvSummaries().getFirst().getSummaryId());
        model.setUserId(dto.getHrvSummaries().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getHrvSummaries().getFirst().getCalendarDate());
        model.setStartTimeInSeconds(dto.getHrvSummaries().getFirst().getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getHrvSummaries().getFirst().getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getHrvSummaries().getFirst().getDurationInSeconds());
        model.setLastNightAvg(dto.getHrvSummaries().getFirst().getLastNightAvg());
        model.setLastNight5MinHigh(dto.getHrvSummaries().getFirst().getLastNight5MinHigh());
        model.setHrvValues(dto.getHrvSummaries().getFirst().getHrvValues());
        return model;
    }

    public GarminStressSummary mapStressPayload(GarminStressSummaryPayload dto, UUID actualizeUserId) {
        GarminStressSummary model = new GarminStressSummary();
        model.setUserId(dto.getStressSummaries().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setSummaryId(dto.getStressSummaries().getFirst().getSummaryId());
        model.setStartTimeInSeconds(dto.getStressSummaries().getFirst().getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStressSummaries().getFirst().getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getStressSummaries().getFirst().getDurationInSeconds());
        model.setCalendarDate(dto.getStressSummaries().getFirst().getCalendarDate());
        model.setTimeOffsetStressLevelValues(dto.getStressSummaries().getFirst().getTimeOffsetStressLevelValues());
        model.setTimeOffsetBodyBatteryValues(dto.getStressSummaries().getFirst().getTimeOffsetBodyBatteryValues());
        model.setBodyBatteryActivityEvents(dto.getStressSummaries().getFirst().getBodyBatteryActivityEventList());
        return model;
    }

    public GarminDailySummary mapDailyPayload(GarminDailySummaryPayload dto, UUID actualizeUserId) {
        GarminDailySummary model = new GarminDailySummary();
        model.setSummaryId(dto.getDailySummaries().getFirst().getSummaryId());
        model.setUserId(dto.getDailySummaries().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getDailySummaries().getFirst().getCalendarDate());
        model.setSummaryId(dto.getDailySummaries().getFirst().getSummaryId());
        model.setSteps(dto.getDailySummaries().getFirst().getSteps());
        model.setStepsGoal(dto.getDailySummaries().getFirst().getStepsGoal());
        model.setDistanceInMeters(dto.getDailySummaries().getFirst().getDistanceInMeters());
        model.setActiveKilocalories(dto.getDailySummaries().getFirst().getActiveKilocalories());
        model.setBmrKilocalories(dto.getDailySummaries().getFirst().getBmrKilocalories());
        model.setMinHeartRate(dto.getDailySummaries().getFirst().getMinHeartRate());
        model.setAverageHeartRate(dto.getDailySummaries().getFirst().getAverageHeartRate());
        model.setMaxHeartRate(dto.getDailySummaries().getFirst().getMaxHeartRate());
        model.setRestingHeartRate(dto.getDailySummaries().getFirst().getRestingHeartRate());
        model.setTimeOffsetHeartRateSamples(dto.getDailySummaries().getFirst().getHeartRateSamples());
        model.setStressDurationInSeconds(dto.getDailySummaries().getFirst().getStressDuration());
        model.setLowStressDurationInSeconds(dto.getDailySummaries().getFirst().getLowStressDuration());
        model.setMediumStressDurationInSeconds(dto.getDailySummaries().getFirst().getMediumStressDuration());
        model.setHighStressDurationInSeconds(dto.getDailySummaries().getFirst().getHighStressDuration());
        model.setDurationInSeconds(dto.getDailySummaries().getFirst().getDurationInSeconds());
        model.setActiveTimeInSeconds(dto.getDailySummaries().getFirst().getActiveTimeInSeconds());
        model.setMaxStressLevel(dto.getDailySummaries().getFirst().getMaxStressLevel());
        model.setAverageStressLevel(dto.getDailySummaries().getFirst().getAverageStressLevel());
        model.setFloorsClimbed(dto.getDailySummaries().getFirst().getFloorsClimbed());
        model.setModerateIntensityDurationInSeconds(dto.getDailySummaries().getFirst().getModerateIntensityDuration());
        model.setVigorousIntensityDurationInSeconds(dto.getDailySummaries().getFirst().getVigorousIntensityDuration());
        return model;
    }

    public GarminPulseOxSummary mapPulseOxPayload(GarminPulseOxSummaryPayload dto, UUID actualizeUserId) {
        GarminPulseOxSummary model = new GarminPulseOxSummary();
        model.setSummaryId(dto.getPulseOxSummaries().getFirst().getSummaryId());
        model.setUserId(dto.getPulseOxSummaries().getFirst().getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getPulseOxSummaries().getFirst().getCalendarDate());
        model.setStartTimeInSeconds(dto.getPulseOxSummaries().getFirst().getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getPulseOxSummaries().getFirst().getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getPulseOxSummaries().getFirst().getDurationInSeconds());
        model.setTimeOffsetSpo2Values(dto.getPulseOxSummaries().getFirst().getSpo2Values());
        return model;
    }
}