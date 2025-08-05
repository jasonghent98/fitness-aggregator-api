
package com.jasonghent98.fitness_aggregator_api.integrations.strava;

import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaUserRepository;
import io.swagger.client.model.SummaryActivity;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.ApiClient;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StravaService {
    StravaUserRepository stravaUserRepo;

    StravaService(StravaUserRepository stravaUserRepo) {
        this.stravaUserRepo = stravaUserRepo;
    }


    public List<SummaryActivity> getActivities(String userAccessToken) {
        ApiClient client = new ApiClient();
        client.setAccessToken(userAccessToken);

        ActivitiesApi activitiesApi = new ActivitiesApi(client);

        try {
            return activitiesApi.getLoggedInAthleteActivities(null, null, 1,30);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava activities", e);
        }
    }
/*
    public List<SummaryActivity> getAthleteStats(String userAccessToken) {
        ApiClient client = new ApiClient();
        client.setAccessToken(userAccessToken);

        AthletesApi athletesApi = new AthletesApi(client);

        try {
            // get the corresponding strava_athlete_id based on userid
            stravaUserRepo.findByUserId();

            return athletesApi.getStats(88624072);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava athlete stats", e);
        }
    }

 */

}