package com.jasonghent98.fitness_aggregator_api.dto.fitbit;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FitbitAuthTokenResponse {
    @JsonProperty("access_token")  public String access_token;
    @JsonProperty("refresh_token") public String refresh_token;
    @JsonProperty("expires_in")    public Integer expires_in;   // seconds
    @JsonProperty("scope")         public String scope;
    @JsonProperty("token_type")    public String token_type;
    @JsonProperty("user_id")       public String user_id;
}