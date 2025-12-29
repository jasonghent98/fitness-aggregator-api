package com.jasonghent98.fitness_aggregator_api.service.sync;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.sync.UserProviderSyncState;
import com.jasonghent98.fitness_aggregator_api.repository.sync.UserProviderSyncStateRepository;
import com.jasonghent98.fitness_aggregator_api.enums.UserProviderSyncBackfillStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SyncUserProviderStateService {
    private final UserProviderSyncStateRepository repo;

    public SyncUserProviderStateService(UserProviderSyncStateRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public UserProviderSyncState getOrCreate(UUID userId, Short providerId, String dataset) {
        return repo.findByUserIdAndProviderIdAndDataset(userId, providerId, dataset)
                .orElseGet(() -> repo.save(
                        UserProviderSyncState.builder()
                                .userId(userId)
                                .providerId(providerId)
                                .dataset(dataset)
                                .backfillStatus(UserProviderSyncBackfillStatus.IDLE)
                                .build()
                ));
    }

    @Transactional
    public void markBackfillRunning(UserProviderSyncState s) {
        s.setBackfillStatus(UserProviderSyncBackfillStatus.RUNNING);
        s.setBackfillStartedAt(Instant.now());
        repo.save(s);
    }

    @Transactional
    public void markBackfillSuccess(UserProviderSyncState s, LocalDate start, LocalDate end) {
        s.setBackfillStatus(UserProviderSyncBackfillStatus.SUCCESS);
        s.setBackfillFinishedAt(Instant.now());
        // expand the covered window
        if (s.getEarliestSyncedDate() == null || start.isBefore(s.getEarliestSyncedDate())) {
            s.setEarliestSyncedDate(start);
        }
        if (s.getLatestSyncedDate() == null || end.isAfter(s.getLatestSyncedDate())) {
            s.setLatestSyncedDate(end);
        }
        repo.save(s);
    }

    @Transactional
    public void markBackfillError(UserProviderSyncState s) {
        s.setBackfillStatus(UserProviderSyncBackfillStatus.ERROR);
        s.setBackfillFinishedAt(Instant.now());
        repo.save(s);
    }

    /** For webhook writes: bump latest/lastIncrementalReceivedAt. */
    @Transactional
    public void recordIncremental(UUID userId, Short providerId, String dataset, LocalDate dataDate) {
        var s = getOrCreate(userId, providerId, dataset);
        if (s.getLatestSyncedDate() == null || dataDate.isAfter(s.getLatestSyncedDate())) {
            s.setLatestSyncedDate(dataDate);
        }
        s.setLastIncrementalReceivedAt(Instant.now());
        repo.save(s);
    }

    /** Retrieves the sync history for a user's providers */
    public List<UserProviderSyncState> getProviderSyncStateForUser() {
        UUID userId = UserContext.getUserId();
        return repo.findByUserId(userId);
    }
}