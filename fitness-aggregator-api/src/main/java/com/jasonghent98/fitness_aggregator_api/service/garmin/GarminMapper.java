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

    public GarminActivitySummary mapActivityPayload(GarminActivitySummaryPayload.ActivitySummary dto, UUID actualizeUserId) {
        GarminActivitySummary model = new GarminActivitySummary();
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setActivityId(dto.getActivityId());
        model.setSummaryId(dto.getSummaryId());
        model.setActivityName(dto.getActivityName());
        model.setActivityType(dto.getActivityType());
        model.setActivityDescription(dto.getActivityDescription());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setStartTimeInSeconds(dto.getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStartTimeOffsetInSeconds());
        model.setAverageBikeCadenceInRoundsPerMinute(dto.getAverageBikeCadenceInRoundsPerMinute());
        model.setAverageHeartRateInBeatsPerMinute(dto.getAverageHeartRateInBeatsPerMinute());
        model.setAverageRunCadenceInStepsPerMinute(dto.getAverageRunCadenceInStepsPerMinute());
        model.setAveragePushCadenceInPushesPerMinute(dto.getAveragePushCadenceInPushesPerMinute());
        model.setAverageSpeedInMetersPerSecond(dto.getAverageSpeedInMetersPerSecond());
        model.setActivityType(dto.getActivityType());
        model.setAverageSwimCadenceInStrokesPerMinute(dto.getAverageSwimCadenceInStrokesPerMinute());
        model.setAveragePaceInMinutesPerKilometer(dto.getAveragePaceInMinutesPerKilometer());
        model.setActiveKilocalories(dto.getActiveKilocalories());
        model.setDistanceInMeters(dto.getDistanceInMeters());
        model.setDeviceName(dto.getDeviceName());
        model.setMaxBikeCadenceInRoundsPerMinute(dto.getMaxBikeCadenceInRoundsPerMinute());
        model.setMaxHeartRateInBeatsPerMinute(dto.getMaxHeartRateInBeatsPerMinute());
        model.setMaxPaceInMinutesPerKilometer(dto.getMaxPaceInMinutesPerKilometer());
        model.setMaxRunCadenceInStepsPerMinute(dto.getMaxRunCadenceInStepsPerMinute());
        model.setMaxPushCadenceInPushesPerMinute(dto.getMaxPushCadenceInPushesPerMinute());
        model.setMaxSpeedInMetersPerSecond(dto.getMaxSpeedInMetersPerSecond());
        model.setStartingLatitudeInDegree(dto.getStartingLatitudeInDegree());
        model.setStartingLongitudeInDegree(dto.getStartingLongitudeInDegree());
        model.setSteps(dto.getSteps());
        model.setPushes(dto.getPushes());
        model.setTotalElevationGainInMeters(dto.getTotalElevationGainInMeters());
        model.setTotalElevationLossInMeters(dto.getTotalElevationLossInMeters());
        model.setManual(dto.getManual());
        model.setIsWebUpload(dto.getIsWebUpload());

        // model.setLaps(dto.getActivitySummary().getFirst().getLap);
        // persist the utc timestamp to Local Date type for easier querying

        Instant instant = Instant.ofEpochSecond(dto.getStartTimeInSeconds());
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

    public GarminDailySummary mapDailyPayload(GarminDailySummaryPayload.DailySummary dto, UUID actualizeUserId) {
        GarminDailySummary model = new GarminDailySummary();
        model.setSummaryId(dto.getSummaryId());
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getCalendarDate());
        model.setSummaryId(dto.getSummaryId());
        model.setSteps(dto.getSteps());
        model.setStepsGoal(dto.getStepsGoal());
        model.setDistanceInMeters(dto.getDistanceInMeters());
        model.setActiveKilocalories(dto.getActiveKilocalories());
        model.setBmrKilocalories(dto.getBmrKilocalories());
        model.setMinHeartRate(dto.getMinHeartRate());
        model.setAverageHeartRate(dto.getAverageHeartRate());
        model.setMaxHeartRate(dto.getMaxHeartRate());
        model.setRestingHeartRate(dto.getRestingHeartRate());
        model.setTimeOffsetHeartRateSamples(dto.getHeartRateSamples());
        model.setStressDurationInSeconds(dto.getStressDuration());
        model.setLowStressDurationInSeconds(dto.getLowStressDuration());
        model.setMediumStressDurationInSeconds(dto.getMediumStressDuration());
        model.setHighStressDurationInSeconds(dto.getHighStressDuration());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setActiveTimeInSeconds(dto.getActiveTimeInSeconds());
        model.setMaxStressLevel(dto.getMaxStressLevel());
        model.setAverageStressLevel(dto.getAverageStressLevel());
        model.setFloorsClimbed(dto.getFloorsClimbed());
        model.setModerateIntensityDurationInSeconds(dto.getModerateIntensityDuration());
        model.setVigorousIntensityDurationInSeconds(dto.getVigorousIntensityDuration());
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