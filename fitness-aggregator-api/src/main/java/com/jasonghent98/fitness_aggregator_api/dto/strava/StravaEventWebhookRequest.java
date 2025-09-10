package com.jasonghent98.fitness_aggregator_api.dto.strava;


import lombok.Data;

import java.util.HashMap;

@Data
public class StravaEventWebhookRequest {

    private String object_type;

    private long object_id;

    private String aspect_type;

    private HashMap<String, String> updates;

    private long owner_id;

    private Integer subscription_id;

    private long event_time;
}
