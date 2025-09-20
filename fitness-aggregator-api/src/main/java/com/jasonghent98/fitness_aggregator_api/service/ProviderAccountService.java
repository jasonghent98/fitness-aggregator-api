package com.jasonghent98.fitness_aggregator_api.service;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProviderAccountService {

    private final ProviderAccountRepository providerAccountRepo;
    private final ProviderRepository providerRepo;
    private final UserRepository userRepo;
    private final TokenRefresherRegistry refresherRegistry;

    public ProviderAccountService(
            ProviderAccountRepository providerAccountRepo,
            ProviderRepository providerRepo,
            UserRepository userRepo,
            TokenRefresherRegistry refresherRegistry
    ) {
        this.providerAccountRepo = providerAccountRepo;
        this.providerRepo = providerRepo;
        this.userRepo = userRepo;
        this.refresherRegistry = refresherRegistry;

    }

    /**
     * Upsert a ProviderAccount and associate it to a User.
     *
     * Resolution strategy (in order):
     * 1) If an account already exists for (provider, providerUserId) -> update tokens & return.
     * 2) Else if userId is provided -> attach new ProviderAccount to that user.
     * 3) Else create a minimal User row, then attach new ProviderAccount.
     */
    @Transactional
    public ProviderAccount upsertProviderAccount(
            UUID userId,               // may be null if caller is unauthenticated
            String providerName,        // provider id (e.g. "1") OR name (e.g. "strava")
            String providerUserId,     // provider's user id/email/username (string to handle all cases)
            String accessToken,
            String refreshToken,
            Instant expiresAt
    ) {
        // 1) Resolve provider (by id or by name)
        Provider provider = resolveProvider(providerName);

        // 2) If an account exists for this provider+providerUserId, update & return
        Optional<ProviderAccount> existing = providerAccountRepo.findByProviderAndProviderUserId(provider, providerUserId);

        if (existing.isPresent()) {
            ProviderAccount acct = existing.get();
            acct.setAccessToken(accessToken);
            acct.setRefreshToken(refreshToken);
            acct.setExpiresAt(expiresAt);
            return providerAccountRepo.save(acct);
        }

        User owner;
        if (userId != null) {
            owner = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        } else {
            // No userId passed and no existing account -> create a minimal user record
            owner = new User();
            owner.setFullName(null);
            owner = userRepo.save(owner);
        }

        // 4) Create new ProviderAccount (no existing found)
        ProviderAccount acct = new ProviderAccount();
        acct.setUser(owner);
        acct.setProvider(provider);
        acct.setProviderUserId(providerUserId);
        acct.setAccessToken(accessToken);
        acct.setRefreshToken(refreshToken);
        acct.setExpiresAt(expiresAt);

        return providerAccountRepo.save(acct);
    }

    /**
     * Gets a valid access token for user+provider + (refreshes if expired)
     * */
    @Transactional
    public String getValidAccessToken(UUID userId, String providerName) {
        Provider provider = resolveProvider(providerName);

        ProviderAccount acct = providerAccountRepo
                .findByUserIdAndProvider(userId, provider)
                .orElseThrow(() -> new IllegalStateException(
                        "No provider account for user=" + userId + " provider=" + provider.getName()));

        if (acct.getExpiresAt() == null || acct.getExpiresAt().isBefore(Instant.now())) {
            var refresher = refresherRegistry.get(provider.getName());
            refresher.refresh(acct);
            providerAccountRepo.save(acct);
        }

        return acct.getAccessToken();
    }

    /**
     * Finds and returns provider accounts for the user
     * */
    public List<ProviderAccount> getProviderAccountsForUser(String userId) {
        return providerAccountRepo.findAllByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("No provider accounts for userId: " + userId));

    }

    /**
     * Finds and returns the provider name or throws if not found
     * */
    private Provider resolveProvider(String providerName) {
        return providerRepo.findByName(providerName)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found by name: " + providerName));

    }

    /**
     * Finds and returns user for a given provider_user_id
     * */
    public ProviderAccount getProviderAccountForUserAndProvider(String providerName, String providerUserId) {
        Provider provider = resolveProvider(providerName);
        return providerAccountRepo.findByProviderAndProviderUserId(provider, providerUserId)
                .orElseThrow(() -> new IllegalArgumentException("No + " + providerName + " account for given provider user id: " + providerUserId));

    }
}