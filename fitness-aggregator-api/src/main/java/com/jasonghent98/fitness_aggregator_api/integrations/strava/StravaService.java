
package com.jasonghent98.fitness_aggregator_api.integrations.strava;

import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaUserRepository;
import io.swagger.client.model.ActivityStats;
import io.swagger.client.model.SummaryActivity;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.ApiClient;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StravaService {
    StravaUserRepository stravaUserRepo;

    StravaService(StravaUserRepository stravaUserRepo) {
        this.stravaUserRepo = stravaUserRepo;
    }

    // returns all activities for a given user
    public List<SummaryActivity> getActivities(String userAccessToken) {
        // set up api client
        ApiClient client = new ApiClient();
        client.setAccessToken(userAccessToken);
        ActivitiesApi activitiesApi = new ActivitiesApi(client);

        try {
            return activitiesApi.getLoggedInAthleteActivities(null, null, 1,30);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava activities", e);
        }
    }

    // returns athlete stats and summaries (swim, bike, run)
    public List<ActivityStats> getAthleteStats(String userAccessToken) {
        // set up api client
        ApiClient client = new ApiClient();
        client.setAccessToken(userAccessToken);
        AthletesApi athletesApi = new AthletesApi(client);


        // Step 2: Get user id from userContext

        // step 3: get user athlete id from userid using stravaUserRepo

        // step 4: make call to athletesApi.getStats with user athlete id


        /*
        try {

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava athlete stats", e);
        }

         */

        return Collections.emptyList(); // temporary placeholder


    }

}