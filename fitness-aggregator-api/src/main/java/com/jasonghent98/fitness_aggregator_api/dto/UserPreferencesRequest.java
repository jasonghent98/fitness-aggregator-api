package com.jasonghent98.fitness_aggregator_api.dto;

import lombok.Data;

@Data
public class UserPreferencesRequest {
    private String personalization;
    private String style;
}
