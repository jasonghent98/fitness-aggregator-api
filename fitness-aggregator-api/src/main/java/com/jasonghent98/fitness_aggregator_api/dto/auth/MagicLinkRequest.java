package com.jasonghent98.fitness_aggregator_api.dto.auth;

import lombok.Data;

@Data
public class MagicLinkRequest {
    private String email;
    // optional: where the user should land after verify (e.g. “/get-started”)
    private String returnTo;
}