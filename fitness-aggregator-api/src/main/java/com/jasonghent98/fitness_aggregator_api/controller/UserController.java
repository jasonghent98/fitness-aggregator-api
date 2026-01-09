package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.UserPreferencesRequest;
import com.jasonghent98.fitness_aggregator_api.dto.user.SyncProviders;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.model.sync.UserProviderSyncState;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderRegistryService;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import com.jasonghent98.fitness_aggregator_api.service.sync.BackfillService;
import com.jasonghent98.fitness_aggregator_api.service.sync.SyncUserProviderStateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final SyncUserProviderStateService svc;
    private final ProviderAccountService providerAccountService;
    private final ProviderRegistryService providerRegistryService;
    private final BackfillService bs;

    UserController(
            UserService userService,
            SyncUserProviderStateService svc,
            ProviderAccountService providerAccountService,
            ProviderRegistryService providerRegistryService,
            BackfillService bs
    ) {
        this.userService = userService;
        this.svc = svc;
        this.providerAccountService = providerAccountService;
        this.providerRegistryService = providerRegistryService;
        this.bs = bs;
    }

    /** Updates user preferences*/
    @PostMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@RequestBody UserPreferencesRequest body) {
        UUID userId = UserContext.getUserId();
        userService.updatePreferences(userId, body.getPersonalization(), body.getStyle(), body.getPersonalization());
        return ResponseEntity.ok().build();
    }

    /** Returns user preferences*/
    @GetMapping("/preferences")
    public ResponseEntity<?> getUserPreferences() {
        UUID userId = UserContext.getUserId();
        User user = userService.getUser(userId);
        return ResponseEntity.ok(
                Map.of("personalization", user.getDashboardPreset(), "style", user.getTrainingStyle())
        );
    }

    /** Returns providers accounts for user */
    @GetMapping("/provider-accounts")
    private List<ProviderAccount> retrieveProviderAccountsForUser() {
         UUID userId = UserContext.getUserId();
        return providerAccountService.getProviderAccountsForUser(userId);
    }

    // SYNC

    /** Returns sync state for provider accounts for user */
    @GetMapping("/sync-state")
    public ResponseEntity<?> getSyncState() {
        List<UserProviderSyncState> syncStates = svc.getProviderSyncStateForUser();

        // Calculate the most recent sync time across all providers and datasets
        Instant lastGlobalSync = syncStates.stream()
                .map(state -> {
                    // Prefer backfillFinishedAt, fallback to lastIncrementalReceivedAt, then updatedAt
                    if (state.getBackfillFinishedAt() != null) {
                        return state.getBackfillFinishedAt();
                    } else if (state.getLastIncrementalReceivedAt() != null) {
                        return state.getLastIncrementalReceivedAt();
                    } else if (state.getUpdatedAt() != null) {
                        return state.getUpdatedAt();
                    }
                    return null;
                })
                .filter(java.util.Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(null);

        return ResponseEntity.ok(Map.of(
                "syncStates", syncStates,
                "lastGlobalSync", lastGlobalSync != null ? lastGlobalSync.toString() : null
        ));
    }

    /** Triggers a user sync for the user*/
    @PostMapping("/sync-state")
    public ResponseEntity<?> triggerSync(@RequestBody SyncProviders syncProviders) {
        UUID userId = UserContext.getUserId();
        String tier = UserContext.getTier();

        // Validate user context
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        // Get tier from user if not in context (fallback)
        if (tier == null || tier.isBlank()) {
            User user = userService.getUser(userId);
            tier = user.getSubscriptionTier() != null ? user.getSubscriptionTier() : "FREE";
        }

        AtomicReference<Map<String, Provider>> codeToProviders = providerRegistryService.getCodeToProvidersCache();

        // Validate providers
        if (syncProviders == null || syncProviders.getProviders() == null || syncProviders.getProviders().length == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "No providers specified"));
        }

        // trigger all provider backfills
        for (String providerCode: syncProviders.getProviders()) {
            Provider provider = codeToProviders.get().get(providerCode);
            if (provider == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid provider: " + providerCode));
            }

            try {
                bs.triggerBackfill(userId, provider, tier);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Provider not connected: " + providerCode));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(Map.of("error", "Sync failed for " + providerCode + ": " + e.getMessage()));
            }
        }

        return ResponseEntity.accepted().body(Map.of("status", "sync_initiated", "providers", syncProviders.getProviders()));
    }
}
