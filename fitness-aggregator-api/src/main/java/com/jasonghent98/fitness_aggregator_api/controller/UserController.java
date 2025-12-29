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
    public List<UserProviderSyncState> getSyncState() {
        return svc.getProviderSyncStateForUser();
    }

    /** Triggers a user sync for the user*/
    @PostMapping("/sync-state")
    public ResponseEntity<?> triggerSync(@RequestBody SyncProviders syncProviders) {
        AtomicReference<Map<String, Provider>> codeToProviders = providerRegistryService.getCodeToProvidersCache();
        // trigger all provider backfills
        for (String p: syncProviders.getProviders()) {
            bs.triggerBackfill(UserContext.getUserId(), codeToProviders.get().get(p), UserContext.getTier());
        }
        return ResponseEntity.accepted().build();
    }
}
