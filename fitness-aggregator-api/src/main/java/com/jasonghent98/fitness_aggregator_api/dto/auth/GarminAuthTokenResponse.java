package com.jasonghent98.fitness_aggregator_api.dto.garmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GarminAuthTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("jti")
    private String jti;

    @JsonProperty("refresh_token_expires_in")
    private long refreshTokenExpiresIn;
}
