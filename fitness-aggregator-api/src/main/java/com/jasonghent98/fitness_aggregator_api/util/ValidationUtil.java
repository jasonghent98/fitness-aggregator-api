package com.jasonghent98.fitness_aggregator_api.util;


public final class ValidationUtil {
    private ValidationUtil() {}

    public static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // lightweight email check good enough for MVP
    public static boolean isValidEmail(String s) {
        return s != null && s.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}