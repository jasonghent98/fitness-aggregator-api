package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.model.garmin.GarminSleepSummary;
import com.jasonghent98.fitness_aggregator_api.repository.garmin.GarminSleepRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class GarminService {

    private final GarminSleepRepository garminSleepRepo;

    GarminService(GarminSleepRepository garminSleepRepo) {
        this.garminSleepRepo = garminSleepRepo;
    }

    public List<Map<String, Object>> getActivities(int page, int size, Instant since) {
        // TODO: return paginated DB results
        return List.of();
    }

    public void triggerSync(String mode) {
        // TODO: enqueue a sync job (recent vs full backfill)
    }

    public void disconnect() {
        // TODO: soft deactivate ProviderAccount
    }

    public List<GarminSleepSummary> getSleepForUserForGivenRange(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return garminSleepRepo.findByUserIdAndCalendarDateBetweenOrderByCalendarDateAsc(userId, startDate, endDate);
    }

    // local dto for getSleepForUserForGivenRange
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

    public record DateRange(LocalDate start, LocalDate end) {}
}