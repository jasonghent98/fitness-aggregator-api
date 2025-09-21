package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import com.jasonghent98.fitness_aggregator_api.repository.garmin.*;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import com.jasonghent98.fitness_aggregator_api.util.WebhookLogger;
import com.jasonghent98.fitness_aggregator_api.util.WebhookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GarminWebhookService {

    private static final Logger log = LoggerFactory.getLogger(GarminWebhookService.class);
    private final GarminDailyRepository garminDailyRepo;
    private final GarminSleepRepository garminSleepRepo;
    private final GarminStressRepository garminStressRepo;
    private final GarminHrvRepository garminHrvRepo;
    private final GarminPulseOxRepository garminPulseOxRepo;
    private final ProviderAccountService providerAccountService;
    private static final String GARMIN_SLEEP_EVENT = "Garmin Sleep Summary";
    private static final String GARMIN_STRESS_EVENT = "Garmin Stress Summary";
    private static final String GARMIN_HRV_EVENT = "Garmin HRV Summary";
    private static final String GARMIN_PULSE_OX_EVENT = "Garmin Pulse Ox Summary";
    private static final String GARMIN_DAILY_EVENT = "Garmin Daily Summary";


    GarminWebhookService(
            GarminDailyRepository garminDailyRepo,
            GarminSleepRepository garminSleepRepo,
            GarminStressRepository garminStressRepo,
            GarminHrvRepository garminHrvRepo,
            GarminPulseOxRepository garminPulseOxRepo,
            ProviderAccountService providerAccountService
    ) {
        this.garminDailyRepo = garminDailyRepo;
        this.garminSleepRepo = garminSleepRepo;
        this.garminStressRepo = garminStressRepo;
        this.garminHrvRepo = garminHrvRepo;
        this.garminPulseOxRepo = garminPulseOxRepo;
        this.providerAccountService = providerAccountService;
    }

    @Async
    public void handleDailyEvents(GarminDailySummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_DAILY_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_DAILY_EVENT,
                    payload,
                    p -> GARMIN_DAILY_EVENT + ": " + p.getDailySummaries().getFirst()
                    );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForUserAndProvider(
                    "garmin",
                    payload.getDailySummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminDailySummary persist = mapDailyPayload(payload, userId);
            garminDailyRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin Daily payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHrvEvents(GarminHrvSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_HRV_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_HRV_EVENT,
                    payload,
                    p -> GARMIN_HRV_EVENT + ": " + p.getHrvSummaries().getFirst()
            );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForUserAndProvider(
                    "garmin",
                    payload.getHrvSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminHrvSummary persist = mapHrvPayload(payload, userId);
            garminHrvRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin HRV payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleHealthEvents(GarminHealthSummaryPayload payload) {
        try {
            /*LEAVE EMPTY FOR NOW*/

        } catch (Exception e) {
            log.error("Error persisting Garmin Health payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handlePulseOxEvents(GarminPulseOxSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_PULSE_OX_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_PULSE_OX_EVENT,
                    payload,
                    p -> GARMIN_PULSE_OX_EVENT + ": " + p.getPulseOxSummaries().getFirst()
            );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForUserAndProvider(
                    "garmin",
                    payload.getPulseOxSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminPulseOxSummary persist = mapPulseOxPayload(payload, userId);
            garminPulseOxRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin Pulse Ox payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleSleepEvents(GarminSleepSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_SLEEP_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_SLEEP_EVENT,
                    payload,
                    p -> GARMIN_SLEEP_EVENT + ": " + p.getSleepSummaries().getFirst()
            );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForUserAndProvider(
                    "garmin",
                    payload.getSleepSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminSleepSummary persist = mapSleepPayload(payload, userId);
            garminSleepRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin Sleep payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
    }

    @Async
    public void handleStressEvents(GarminStressSummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_STRESS_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_STRESS_EVENT,
                    payload,
                    p -> GARMIN_STRESS_EVENT + ": " + p.getStressSummaries().getFirst()
            );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForUserAndProvider(
                    "garmin",
                    payload.getStressSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminStressSummary persist = mapStressPayload(payload, userId);
            garminStressRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin Stress payloads", e);
        }
    }


    private GarminSleepSummary mapSleepPayload(GarminSleepSummaryPayload dto, UUID actualizeUserId) {
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

    private GarminHrvSummary mapHrvPayload(GarminHrvSummaryPayload dto, UUID actualizeUserId) {
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

    private GarminStressSummary mapStressPayload(GarminStressSummaryPayload dto, UUID actualizeUserId) {
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

    private GarminDailySummary mapDailyPayload(GarminDailySummaryPayload dto, UUID actualizeUserId) {
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

    private GarminPulseOxSummary mapPulseOxPayload(GarminPulseOxSummaryPayload dto, UUID actualizeUserId) {
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