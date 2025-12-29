package com.jasonghent98.fitness_aggregator_api.integrations;

import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;


/**
 * Provider-agnostic auth-handling interface to be implemented by each provider for refresh logic
 * */

public interface TokenRefresher {
    /** return an updated ProviderAccount (mutated or new instance) with fresh tokens/expiresAt set */
    ProviderAccount refresh(ProviderAccount acct);
    /** e.g. "strava", "fitbit", "garmin" */
    String providerName();
}