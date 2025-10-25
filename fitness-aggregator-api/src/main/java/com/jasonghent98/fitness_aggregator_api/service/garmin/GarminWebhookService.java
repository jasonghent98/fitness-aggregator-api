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
    private final GarminActivityRepository garminActivityRepo;
    private final ProviderAccountService providerAccountService;
    private static final String GARMIN_SLEEP_EVENT = "Garmin Sleep Summary";
    private static final String GARMIN_STRESS_EVENT = "Garmin Stress Summary";
    private static final String GARMIN_HRV_EVENT = "Garmin HRV Summary";
    private static final String GARMIN_PULSE_OX_EVENT = "Garmin Pulse Ox Summary";
    private static final String GARMIN_DAILY_EVENT = "Garmin Daily Summary";
    private static final String GARMIN_ACTIVITY_EVENT = "Garmin Activity Summary";
    private final GarminMapper garminMapper;

    GarminWebhookService(
            GarminDailyRepository garminDailyRepo,
            GarminSleepRepository garminSleepRepo,
            GarminStressRepository garminStressRepo,
            GarminHrvRepository garminHrvRepo,
            GarminPulseOxRepository garminPulseOxRepo,
            ProviderAccountService providerAccountService,
            GarminActivityRepository garminActivityRepo,
            GarminMapper garminMapper
    ) {
        this.garminDailyRepo = garminDailyRepo;
        this.garminSleepRepo = garminSleepRepo;
        this.garminStressRepo = garminStressRepo;
        this.garminHrvRepo = garminHrvRepo;
        this.garminPulseOxRepo = garminPulseOxRepo;
        this.garminActivityRepo = garminActivityRepo;
        this.providerAccountService = providerAccountService;
        this.garminMapper = garminMapper;
    }

    @Async
    public void handleActivityEvents(GarminActivitySummaryPayload payload) {
        try {
            WebhookValidator.requireNonEmpty(payload, GARMIN_ACTIVITY_EVENT);
            WebhookLogger.logWebhookEvent(
                    log,
                    GARMIN_ACTIVITY_EVENT,
                    payload,
                    p -> GARMIN_ACTIVITY_EVENT + ": " + p
            );
            // get the provider user id to our platform user id to store for faster retrievals
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getActivitySummary().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            for (GarminActivitySummaryPayload.ActivitySummary d: payload.getActivitySummary()) {
                GarminActivitySummary persist = garminMapper.mapActivityPayload(d, userId);
                garminActivityRepo.save(persist);
            }

        } catch (Exception e) {
            log.error("Error persisting Garmin Activity payload", e);
            // Optional: persist to a dead-letter table for later retry
        }
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
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getDailySummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            for (GarminDailySummaryPayload.DailySummary d : payload.getDailySummaries()) {
                GarminDailySummary persist = garminMapper.mapDailyPayload(d, userId);
                garminDailyRepo.save(persist);
            }

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
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getHrvSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminHrvSummary persist = garminMapper.mapHrvPayload(payload, userId);
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
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getPulseOxSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminPulseOxSummary persist = garminMapper.mapPulseOxPayload(payload, userId);
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
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getSleepSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminSleepSummary persist = garminMapper.mapSleepPayload(payload, userId);
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
            ProviderAccount garminAcc = providerAccountService.getProviderAccountForProviderUserAndProvider(
                    "garmin",
                    payload.getStressSummaries().getFirst().getUserId()
            );
            UUID userId = garminAcc.getUser().getId();
            // persist
            GarminStressSummary persist = garminMapper.mapStressPayload(payload, userId);
            garminStressRepo.save(persist);

        } catch (Exception e) {
            log.error("Error persisting Garmin Stress payloads", e);
        }
    }

}