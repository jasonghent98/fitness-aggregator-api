package com.jasonghent98.fitness_aggregator_api.dto.garmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GarminAPIUserId {
    @JsonProperty("userId")
    private String userId;
}
