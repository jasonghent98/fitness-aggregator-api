package com.jasonghent98.fitness_aggregator_api.service.garmin;

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
        // Fetch Garmin link for this user
        ProviderAccount garmin = providerAccounts.getProviderAccountForUserAndProvider("garmin", actualizeUserId);
        String providerUserId = garmin.getProviderUserId();
        Short providerId = garmin.getProvider().getId(); // FK to providers table

        backfillDaily(actualizeUserId, providerId, providerUserId, start, end);
        backfillSleep(actualizeUserId, providerId, providerUserId, start, end);
        backfillStress(actualizeUserId, providerId, providerUserId, start, end);
        backfillHrv(actualizeUserId, providerId, providerUserId, start, end);
        backfillPulseOx(actualizeUserId, providerId, providerUserId, start, end);
        backfillActivities(actualizeUserId, providerId, providerUserId, start, end);
    }

    @Transactional
    public void backfillDaily(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        // upsert a sync state record in db for race conditions
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "daily");
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
    public void backfillSleep(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "sleep");
        syncState.markBackfillRunning(s);
        try {
            for (var it : api.fetchSleep(userId, start, end)) {
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
    public void backfillStress(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "stress");
        syncState.markBackfillRunning(s);
        try {
            for (var it : api.fetchStress(userId, start, end)) {
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
    public void backfillHrv(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "hrv");
        syncState.markBackfillRunning(s);
        try {
            for (var it : api.fetchHrv(userId, start, end)) {
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
    public void backfillPulseOx(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "pulse_ox");
        syncState.markBackfillRunning(s);
        try {
            for (var it : api.fetchPulseOx(userId, start, end)) {
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
    public void backfillActivities(UUID userId, Short providerId, String provUserId, LocalDate start, LocalDate end) {
        UserProviderSyncState s = syncState.getOrCreate(userId, providerId, "activity");
        syncState.markBackfillRunning(s);
        try {
            for (var it : api.fetchActivities(userId, start, end)) {
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