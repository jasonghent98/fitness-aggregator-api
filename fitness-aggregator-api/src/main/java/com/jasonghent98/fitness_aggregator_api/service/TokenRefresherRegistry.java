// src/main/java/.../provider/TokenRefresherRegistry.java
package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.integrations.TokenRefresher;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenRefresherRegistry {
    private final Map<String, TokenRefresher> byName;

    public TokenRefresherRegistry(java.util.List<TokenRefresher> refreshers) {
        this.byName = refreshers.stream()
                .collect(Collectors.toMap(
                        r -> r.providerName().toLowerCase(),
                        r -> r
                ));
    }

    public TokenRefresher get(String providerName) {
        TokenRefresher r = byName.get(providerName.toLowerCase());
        if (r == null) {
            throw new IllegalArgumentException("No TokenRefresher registered for provider=" + providerName);
        }
        return r;
    }
}