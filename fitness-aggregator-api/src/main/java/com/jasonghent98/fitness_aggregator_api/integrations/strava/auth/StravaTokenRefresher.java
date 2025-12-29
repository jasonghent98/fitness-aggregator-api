// src/main/java/.../provider/strava/StravaTokenRefresher.java
package com.jasonghent98.fitness_aggregator_api.provider.strava;

import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.integrations.TokenRefresher;
import com.jasonghent98.fitness_aggregator_api.service.strava.StravaTokenClient;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class StravaTokenRefresher implements TokenRefresher {

    private final StravaTokenClient client;

    public StravaTokenRefresher(StravaTokenClient client) {
        this.client = client;
    }

    @Override
    public ProviderAccount refresh(ProviderAccount acct) {
        StravaAuthTokenResponse r = client.refreshToken(acct.getRefreshToken());
        acct.setAccessToken(r.accessToken);
        acct.setRefreshToken(r.refreshToken);
        acct.setExpiresAt(Instant.ofEpochSecond(r.expiresAt));
        return acct;
    }

    @Override
    public String providerName() {
        return "strava";
    }
}