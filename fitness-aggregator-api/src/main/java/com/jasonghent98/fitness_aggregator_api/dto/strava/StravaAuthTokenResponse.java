package com.jasonghent98.fitness_aggregator_api.dto.strava;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StravaAuthTokenResponse {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("expires_at")
    public long expiresAt;

    @JsonProperty("athlete")
    public StravaAthlete athlete;

    public static class StravaAthlete {
        @JsonProperty("id")
        public long id;

        @JsonProperty("username")
        public String username;

        @JsonProperty("firstname")
        public String firstName;

        @JsonProperty("lastname")
        public String lastName;
    }
}
