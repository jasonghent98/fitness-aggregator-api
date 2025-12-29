package com.jasonghent98.fitness_aggregator_api.integrations.fitbit.auth;

import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.integrations.TokenRefresher;
import org.springframework.stereotype.Component;

import java.time.Instant;

/*

-- Need to define the FitbitTokenClass for this to work --


@Component
public class FitbitTokenRefresher implements TokenRefresher {

    private final FitbitTokenClient client; // implement similar to Strava client

    public FitbitTokenRefresher(FitbitTokenClient client) {
        this.client = client;
    }

    @Override
    public ProviderAccount refresh(ProviderAccount acct) {
        var r = client.refresh(acct.getRefreshToken());
        acct.setAccessToken(r.accessToken());
        acct.setRefreshToken(r.refreshToken());
        acct.setExpiresAt(Instant.now().plusSeconds(r.expiresIn()));
        return acct;
    }

    @Override
    public String providerName() {
        return "fitbit";
    }
}

 */