package com.jasonghent98.fitness_aggregator_api.integrations;

import com.strava.api.v3.*;
import com.strava.api.v3.auth.*;
import com.strava.api.v3.model.*;
import com.strava.api.v3.api.ActivitiesApi;

import rx.Observable;

public class Strava {
    public statis void main(String... args) {
        System.out.println("Strava API");
    }
    public static void main(String... args) {
        ApiClient client = new ApiClient(...);
        ActivitiesApi api = client.createService(ActivitiesApi.class);

        Integer before = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place before a certain time.
        Integer after = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place after a certain time.
        Integer page = 56; // Integer | Page number. Defaults to 1.
        Integer perPage = 56; // Integer | Number of items per page. Defaults to 30.

        Observable<List<SummaryActivity>> result = apiInstance.getLoggedInAthleteActivities(before, after, page, perPage);
        result.subscribe(System.out::println, Throwable::printStackTrace);
    }
}
