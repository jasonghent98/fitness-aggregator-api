package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.*;
import com.jasonghent98.fitness_aggregator_api.repository.garmin.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GarminService {

    private final GarminSleepRepository garminSleepRepo;
    private final GarminStressRepository garminStressRepo;
    private final GarminHrvRepository garminHrvRepo;
    private final GarminDailyRepository garminDailyRepo;
    private final GarminPulseOxRepository garminPulseOxRepo;
    private final GarminActivityRepository garminActivityRepo;

    GarminService(
            GarminSleepRepository garminSleepRepo,
            GarminStressRepository garminStressRepo,
            GarminHrvRepository garminHrvRepo,
            GarminDailyRepository garminDailyRepo,
            GarminPulseOxRepository garminPulseOxRepo,
            GarminActivityRepository garminActivityRepo
    ) {
        this.garminSleepRepo = garminSleepRepo;
        this.garminStressRepo = garminStressRepo;
        this.garminHrvRepo = garminHrvRepo;
        this.garminDailyRepo = garminDailyRepo;
        this.garminPulseOxRepo = garminPulseOxRepo;
        this.garminActivityRepo = garminActivityRepo;
    }


    public void triggerSync(String mode) {
        // TODO: enqueue a sync job (recent vs full backfill)
    }

    public void disconnect() {
        // TODO: soft deactivate ProviderAccount
    }

    /** Returns activity data for user for given range */
    public List<GarminActivitySummary> getActivityForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminActivityRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }

    /** Returns sleep data for user for given range */
    public List<GarminSleepSummary> getSleepForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminSleepRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }

    /** Returns stress data for user for given range */
    public List<GarminStressSummary> getStressForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminStressRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }


    /** Returns hrv data for user for given range */
    public List<GarminHrvSummary> getHrvForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminHrvRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }

    /** Returns daily data for user for given range */
    public List<GarminDailySummary> getDailyForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminDailyRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }

    /** Returns pulse ox data for user for given range */
    public List<GarminPulseOxSummary> getPulseOxForUserForGivenRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminPulseOxRepo.findByActualizeUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }


    /** Resolves range or start/end date depending on what time frame user wants for data */
    public DateRange resolveRange(String range, LocalDate startDate, LocalDate endDate, int maxDays) {
        LocalDate today = LocalDate.now();

        if (startDate != null && endDate != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            if (days > maxDays) {
                throw new IllegalArgumentException("Requested range exceeds allowed history window of " + maxDays + " days");
            }
            return new DateRange(startDate, endDate);
        }

        if (range != null) {
            switch (range) {
                case "1d": return new DateRange(today.minusDays(1), today);
                case "7d": return new DateRange(today.minusDays(7), today);
                case "14d": return new DateRange(today.minusDays(14), today);
                case "30d": return new DateRange(today.minusDays(30), today);
                case "90d": return new DateRange(today.minusDays(90), today);
            }
        }

        // Default: max window allowed
        return new DateRange(today.minusDays(maxDays), today);
    }

    /** Local dto for getSleepForUserForGivenRange */
    public record DateRange(LocalDate start, LocalDate end) {}
}