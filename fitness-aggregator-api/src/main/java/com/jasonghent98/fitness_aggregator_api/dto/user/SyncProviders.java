package com.jasonghent98.fitness_aggregator_api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SyncProviders {
    @JsonProperty("providers")
    private String[] providers;
}
