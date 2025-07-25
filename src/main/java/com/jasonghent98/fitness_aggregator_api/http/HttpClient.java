package com.jasonghent98.fitness_aggregator_api.http;

/*HttpClient interface to serve as middle layer between service classes (garmin, strava, etc..) and actual http library*/
public interface HttpClient {
    <T> T get(String url, Class<T> responseType, String accessToken);
}