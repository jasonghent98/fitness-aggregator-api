package com.jasonghent98.fitness_aggregator_api.service.sync;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TierWindowService {
    @Value("${sync.window.free.days:30}")     private int freeDays;
    @Value("${sync.window.enhanced.days:180}") private int enhancedDays;
    @Value("${sync.window.elite.days:1095}")   private int eliteDays; // ~3y

    public int windowDays(String tier) {
        if (tier == null) return freeDays;
        return switch (tier.toUpperCase()) {
            case "ENHANCED" -> enhancedDays;
            case "ELITE"    -> eliteDays;
            default         -> freeDays;
        };
    }

    /** Compute backfill window, clamped to [today-window, today]. */
    public DateWindow computeWindow(String tier) {
        int days = windowDays(tier);
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(days - 1);
        return new DateWindow(start, end);
    }

    public record DateWindow(LocalDate start, LocalDate end) {}
}