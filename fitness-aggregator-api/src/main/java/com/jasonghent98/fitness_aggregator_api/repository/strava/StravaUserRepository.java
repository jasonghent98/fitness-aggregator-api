package com.jasonghent98.fitness_aggregator_api.repository.strava;

import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

// methods that JPA will implement during build
public interface StravaUserRepository extends JpaRepository<StravaUser, UUID> {
    Optional<StravaUser> findByStravaAthleteId(Long stravaAthleteId);
    Optional<StravaUser> findByUser(User user);
    Optional<StravaUser> findByUserId(UUID userId);
}
