
package com.jasonghent98.fitness_aggregator_api.integrations.strava;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaUserRepository;
import io.swagger.client.model.ActivityStats;
import io.swagger.client.model.SummaryActivity;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.api.AthletesApi;
import io.swagger.client.ApiClient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public ActivityStats getAthleteStats(String userAccessToken) {
        // set up api client
        ApiClient client = new ApiClient();
        client.setAccessToken(userAccessToken);
        AthletesApi athletesApi = new AthletesApi(client);


        // Step 2: Get user id from userContext
        UUID userId = UserContext.getUserId();

        // step 3: get user athlete id from userid using stravaUserRepo
        StravaUser stravaUser = stravaUserRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Strava user not found for userId: " + userId));
        Long stravaAthleteId = stravaUser.getStravaAthleteId();

        // step 4: make call to athletesApi.getStats with user athlete id
        try {
            return athletesApi.getStats(stravaAthleteId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava athlete stats", e);
        }

    }

}