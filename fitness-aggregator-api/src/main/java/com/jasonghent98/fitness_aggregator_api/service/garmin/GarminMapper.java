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


    public GarminSleepSummary mapSleepPayload(GarminSleepSummaryPayload.SleepSummary dto, UUID actualizeUserId) {
        GarminSleepSummary model = new GarminSleepSummary();
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setSummaryId(dto.getSummaryId());
        model.setCalendarDate(dto.getCalendarDate());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setDeepSleepDurationInSeconds(dto.getDeepSleepDurationInSeconds());
        model.setLightSleepDurationInSeconds(dto.getLightSleepDurationInSeconds());
        model.setRemSleepInSeconds(dto.getRemSleepInSeconds());
        model.setAwakeDurationInSeconds(dto.getAwakeDurationInSeconds());
        model.setNaps(dto.getNaps());
        model.setStartTimeInSeconds(dto.getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStartTimeOffsetInSeconds());
        model.setOverallSleepScore(dto.getOverallSleepScore());
        model.setSleepScores(dto.getSleepScores());
        return model;
    }

    public GarminHrvSummary mapHrvPayload(GarminHrvSummaryPayload.HrvSummary dto, UUID actualizeUserId) {
        GarminHrvSummary model = new GarminHrvSummary();
        model.setSummaryId(dto.getSummaryId());
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getCalendarDate());
        model.setStartTimeInSeconds(dto.getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setLastNightAvg(dto.getLastNightAvg());
        model.setLastNight5MinHigh(dto.getLastNight5MinHigh());
        model.setHrvValues(dto.getHrvValues());
        return model;
    }

    public GarminStressSummary mapStressPayload(GarminStressSummaryPayload.StressSummary dto, UUID actualizeUserId) {
        GarminStressSummary model = new GarminStressSummary();
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setSummaryId(dto.getSummaryId());
        model.setStartTimeInSeconds(dto.getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setCalendarDate(dto.getCalendarDate());
        model.setTimeOffsetStressLevelValues(dto.getTimeOffsetStressLevelValues());
        model.setTimeOffsetBodyBatteryValues(dto.getTimeOffsetBodyBatteryValues());
        model.setBodyBatteryActivityEvents(dto.getBodyBatteryActivityEventList());
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

    public GarminPulseOxSummary mapPulseOxPayload(GarminPulseOxSummaryPayload.PulseOxSummary dto, UUID actualizeUserId) {
        GarminPulseOxSummary model = new GarminPulseOxSummary();
        model.setSummaryId(dto.getSummaryId());
        model.setUserId(dto.getUserId());
        model.setActualizeUserId(actualizeUserId);
        model.setCalendarDate(dto.getCalendarDate());
        model.setStartTimeInSeconds(dto.getStartTimeInSeconds());
        model.setStartTimeOffsetInSeconds(dto.getStartTimeOffsetInSeconds());
        model.setDurationInSeconds(dto.getDurationInSeconds());
        model.setTimeOffsetSpo2Values(dto.getSpo2Values());
        return model;
    }
}