package com.jasonghent98.fitness_aggregator_api.seed;

import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Configuration
public class ProviderSeed {

    @Bean
    ApplicationRunner seedProviders(ProviderRepository repo) {
        return args -> upsertProviders(repo);
    }

    @Transactional
    void upsertProviders(ProviderRepository repo) {
        // name -> (authType, authorizeUrl, tokenUrl, defaultScopes, docsUrl, enabled)
        Map<String, ProviderSpec> specs = Map.of(
                "strava", new ProviderSpec(
                        "oauth2",
                        "https://www.strava.com/oauth/authorize",
                        "https://www.strava.com/oauth/token",
                        "read,activity:read",
                        "https://developers.strava.com/docs/authentication/",
                        true
                ),
                "fitbit", new ProviderSpec(
                        "oauth2",
                        "https://www.fitbit.com/oauth2/authorize",
                        "https://api.fitbit.com/oauth2/token",
                        "activity heartrate sleep profile",
                        "https://dev.fitbit.com/build/reference/web-api/developer-guide/authorization/",
                        true
                ),
                "garmin", new ProviderSpec(
                        "oauth1",
                        "https://connect.garmin.com/oauthConfirm",
                        "https://connectapi.garmin.com/oauth-service/oauth/access_token",
                        null,
                        "https://developer.garmin.com/",
                        false
                ),
                "whoop", new ProviderSpec(
                        "oauth2",
                        "https://api.prod.whoop.com/oauth/oauth2/auth",
                        "https://api.prod.whoop.com/oauth/oauth2/token",
                        "offline read:recovery read:cycles read:workout read:sleep",
                        "https://developer.whoop.com/",
                        false
                ),
                "oura", new ProviderSpec(
                        "oauth2",
                        "https://cloud.ouraring.com/oauth/authorize",
                        "https://api.ouraring.com/oauth/token",
                        "email personal daily heartrate workout",
                        "https://cloud.ouraring.com/docs/authentication",
                        false
                )
        );

        specs.forEach((name, s) -> {
            Provider p = repo.findByName(name).orElseGet(Provider::new);
            p.setName(name);
            p.setAuthType(s.authType);
            p.setAuthorizeUrl(s.authorizeUrl);
            p.setTokenUrl(s.tokenUrl);
            p.setDefaultScopes(s.defaultScopes);
            p.setDocsUrl(s.docsUrl);
            p.setEnabled(s.enabled);
            repo.save(p); // upsert (insert or update)
        });
    }

    private record ProviderSpec(
            String authType,
            String authorizeUrl,
            String tokenUrl,
            String defaultScopes,
            String docsUrl,
            boolean enabled
    ) {}
}