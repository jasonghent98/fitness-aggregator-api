// util/DateRangeUtil.java
package com.jasonghent98.fitness_aggregator_api.util;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

public final class DateRangeUtil {
    private DateRangeUtil() {}

    /** Clamp/custom ranges, honoring maxDays (inclusive window). */
    public static DateRange resolve(String range, LocalDate startDate, LocalDate endDate, int maxDays) {
        LocalDate today = LocalDate.now();

        if (startDate != null && endDate != null) {
            long days = DAYS.between(startDate, endDate);
            if (days < 0) throw new IllegalArgumentException("startDate must be ≤ endDate");
            if (days + 1 > maxDays) throw new IllegalArgumentException("Requested range exceeds allowed window of " + maxDays + " days");
            return new DateRange(startDate, endDate);
        }

        if (range != null) {
            return switch (range.toLowerCase()) {
                case "1d"  -> new DateRange(today.minusDays(1),  today);
                case "7d"  -> new DateRange(today.minusDays(7),  today);
                case "14d" -> new DateRange(today.minusDays(14), today);
                case "30d" -> new DateRange(today.minusDays(30), today);
                case "90d" -> new DateRange(today.minusDays(90), today);
                case "365d"-> new DateRange(today.minusDays(365),today);
                default    -> new DateRange(today.minusDays(Math.min(maxDays, 30)), today); // sane default
            };
        }

        // Default → max window
        return new DateRange(today.minusDays(maxDays - 1), today);
    }

    public record DateRange(LocalDate start, LocalDate end) {}

}