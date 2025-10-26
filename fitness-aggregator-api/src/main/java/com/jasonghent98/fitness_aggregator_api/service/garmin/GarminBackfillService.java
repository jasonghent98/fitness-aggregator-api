package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import com.jasonghent98.fitness_aggregator_api.model.sync.UserProviderSyncState;
import com.jasonghent98.fitness_aggregator_api.repository.garmin.*;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import com.jasonghent98.fitness_aggregator_api.service.sync.SyncUserProviderStateService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class GarminBackfillService {
    private static final Logger log = LoggerFactory.getLogger(GarminBackfillService.class);

    private final GarminApiClient api;
    private final GarminMapper mapper;
    private final ProviderAccountService providerAccounts;
    private final SyncUserProviderStateService syncState;

    private final GarminDailyRepository dailyRepo;
    private final GarminSleepRepository sleepRepo;
    private final GarminStressRepository stressRepo;
    private final GarminHrvRepository hrvRepo;
    private final GarminPulseOxRepository pulseRepo;
    private final GarminActivityRepository activityRepo;
    // ! avoid db call but make sure this always remains constant
    private final Short GARMIN_PROVIDER_ID = 1;

    public GarminBackfillService(
            GarminApiClient api,
            GarminMapper mapper,
            ProviderAccountService providerAccounts,
            SyncUserProviderStateService syncState,
            GarminDailyRepository dailyRepo,
            GarminSleepRepository sleepRepo,
            GarminStressRepository stressRepo,
            GarminHrvRepository hrvRepo,
            GarminPulseOxRepository pulseRepo,
            GarminActivityRepository activityRepo
    ) {
        this.api = api;
        this.mapper = mapper;
        this.providerAccounts = providerAccounts;
        this.syncState = syncState;
        this.dailyRepo = dailyRepo;
        this.sleepRepo = sleepRepo;
        this.stressRepo = stressRepo;
        this.hrvRepo = hrvRepo;
        this.pulseRepo = pulseRepo;
        this.activityRepo = activityRepo;
    }

    @Async
    public void backfillAll(UUID actualizeUserId, LocalDate start, LocalDate end) {
        System.out.println("running GarminBackfillService");
        backfillDaily(actualizeUserId, start, end);
        backfillSleep(actualizeUserId, start, end);
        backfillStress(actualizeUserId, start, end);
        backfillHrv(actualizeUserId, start, end);
        backfillPulseOx(actualizeUserId,  start, end);
        backfillActivities(actualizeUserId,  start, end);
        System.out.println("finishing GarminBackfillService");
    }

    @Transactional
    public void backfillDaily(UUID userId, LocalDate start, LocalDate end) {
        // upsert a sync state record in db for race conditions
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "daily");
        syncState.markBackfillRunning(s);
        try {
            for (var obj : api.fetchDaily(userId, start, end)) {
                try { dailyRepo.save(mapper.mapDailyPayload(obj, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin daily backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }

    @Transactional
    public void backfillSleep(UUID userId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "sleep");
        syncState.markBackfillRunning(s);
        try {
            for (GarminSleepSummaryPayload.SleepSummary d : api.fetchSleep(userId, start, end)) {
                /*try { sleepRepo.save(mapper.toSleep(it, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}*/
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin sleep backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }

    @Transactional
    public void backfillStress(UUID userId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "stress");
        syncState.markBackfillRunning(s);
        try {
            for (GarminStressSummaryPayload.StressSummary d: api.fetchStress(userId, start, end)) {
                /*try { stressRepo.save(mapper.toStress(it, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}*/
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin stress backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }

    @Transactional
    public void backfillHrv(UUID userId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "hrv");
        syncState.markBackfillRunning(s);
        try {
            for (GarminHrvSummaryPayload.HrvSummary it : api.fetchHrv(userId, start, end)) {
                /*try { hrvRepo.save(mapper.toHrv(it, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}*/
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin HRV backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }

    @Transactional
    public void backfillPulseOx(UUID userId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "pulse_ox");
        syncState.markBackfillRunning(s);
        try {
            for (GarminPulseOxSummaryPayload.PulseOxSummary d : api.fetchPulseOx(userId, start, end)) {
                /*try { pulseRepo.save(mapper.toPulseOx(it, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}*/
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin PulseOx backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }

    @Transactional
    public void backfillActivities(UUID userId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, GARMIN_PROVIDER_ID, "activity");
        syncState.markBackfillRunning(s);
        try {
            for (GarminActivitySummaryPayload.ActivitySummary d : api.fetchActivities(userId, start, end)) {
                /*try { activityRepo.save(mapper.toActivity(it, userId)); }
                catch (DataIntegrityViolationException ignoreDup) {}*/
            }
            syncState.markBackfillSuccess(s, start, end);
        } catch (Exception ex) {
            log.error("Garmin activities backfill failed", ex);
            syncState.markBackfillError(s);
        }
    }
}