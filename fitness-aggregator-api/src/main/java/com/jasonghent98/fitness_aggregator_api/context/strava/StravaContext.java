package com.jasonghent98.fitness_aggregator_api.context.strava;

public class StravaContext {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setAccessToken(String token) {
        tokenHolder.set(token);
    }

    public static String getAccessToken() {
        return tokenHolder.get();
    }

    public static void clear() {
        tokenHolder.remove(); // VERY important to prevent memory leaks
    }
}
