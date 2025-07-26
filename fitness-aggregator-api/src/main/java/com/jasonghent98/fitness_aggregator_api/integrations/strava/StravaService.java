
package com.jasonghent98.fitness_aggregator_api.integrations.strava;

import io.swagger.client.model.SummaryActivity;
import io.swagger.client.api.ActivitiesApi;
import io.swagger.client.ApiClient;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StravaService {

    public List<SummaryActivity> getActivities(String accessToken) {
        io.swagger.client.ApiClient client = new ApiClient();
        client.setAccessToken(accessToken);

        ActivitiesApi activitiesApi = new ActivitiesApi(client);

        try {
            return activitiesApi.getLoggedInAthleteActivities(null, null, 1,30);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Strava activities", e);
        }
    }

}