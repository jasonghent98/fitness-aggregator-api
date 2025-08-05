package com.jasonghent98.fitness_aggregator_api.service.strava;

import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaUserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class StravaAuthService {
    private final StravaUserRepository stravaUserRepo;
    private final StravaTokenClient stravaTokenClient;

    public StravaAuthService(StravaUserRepository repo, StravaTokenClient client) {
        this.stravaUserRepo = repo;
        this.stravaTokenClient = client;
    }

    public String getValidAccessToken(UUID userId) {
        StravaUser user = stravaUserRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Strava user not found"));

        if (user.getExpiresAt().isBefore(Instant.now())) {
            StravaAuthTokenResponse refreshed = stravaTokenClient.refreshToken(user.getRefreshToken());
            user.setAccessToken(refreshed.accessToken);
            user.setRefreshToken(refreshed.refreshToken);
            user.setExpiresAt(Instant.ofEpochSecond(refreshed.expiresAt));

            stravaUserRepo.save(user);
        }

        return user.getAccessToken();
    }
}