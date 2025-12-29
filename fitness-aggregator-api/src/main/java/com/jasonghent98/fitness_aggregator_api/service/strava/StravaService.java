package com.jasonghent98.fitness_aggregator_api.service.strava;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaEventWebhookRequest;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaActivity;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaStats;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaActivityRepository;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaStatsRepository;
import org.springframework.stereotype.Service;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaActivity;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaStats;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaActivityRepository;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaStatsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


import java.util.List;
import java.util.UUID;

// All data is retrieved from DB directly (no API calls)
@Service
public class StravaService {
    StravaActivityRepository stravaActRepo;
    StravaStatsRepository stravaStatsRepo;

    StravaService(
            StravaActivityRepository stravaActRepo,
            StravaStatsRepository stravaStatsRepo
    ) {
        this.stravaActRepo = stravaActRepo;
        this.stravaStatsRepo = stravaStatsRepo;
    }

    // Returns all activities for a given user from our DB
    public List<StravaActivity> getActivitiesForUser() {
        UUID userId = UserContext.getUserId();
        return stravaActRepo.findByUserIdOrderByStartDateDesc(userId);
    }

    // returns athlete stats and summaries (swim, bike, run) from our DB
    public List<StravaStats> getStatsForUser() {
        UUID userId = UserContext.getUserId();
        return stravaStatsRepo.findByUserId(userId);

    }

    // WEBHOOKS AND SERVER EVENTS //

    public void onActivityCreate(StravaEventWebhookRequest evt) {
        // enqueue/background: fetch /activities/{object_id} with athlete context (owner_id) and persist
    }

    public void onActivityUpdate(StravaEventWebhookRequest evt) {
        // mark activity dirty and refetch, or selectively update from evt.getUpdates()
    }

    public void onActivityDelete(StravaEventWebhookRequest evt) {
        // soft-delete or mark as removed in your DB for object_id / owner_id
    }

    public void onAthleteEvent(StravaEventWebhookRequest evt) {
        // if updates.authorized == "false": deactivate ProviderAccount for owner_id
    }

}