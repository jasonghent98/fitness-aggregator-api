package com.jasonghent98.fitness_aggregator_api.repository.sync;

import com.jasonghent98.fitness_aggregator_api.model.sync.UserProviderSyncState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProviderSyncStateRepository extends JpaRepository<UserProviderSyncState, UUID> {

    Optional<UserProviderSyncState> findByUserIdAndProviderIdAndDataset(
            UUID userId, Short providerId, String dataset);

    List<UserProviderSyncState> findByUserId(UUID userId);

    List<UserProviderSyncState> findByUserIdAndProviderId(UUID userId, Short providerId);
}