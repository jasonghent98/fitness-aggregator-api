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
import java.util.Optional;
import java.util.UUID;

@Service
public class ProviderAccountService {

    private final ProviderAccountRepository providerAccountRepo;
    private final ProviderRepository providerRepo;
    private final UserRepository userRepo;

    public ProviderAccountService(ProviderAccountRepository providerAccountRepo,
                                  ProviderRepository providerRepo,
                                  UserRepository userRepo) {
        this.providerAccountRepo = providerAccountRepo;
        this.providerRepo = providerRepo;
        this.userRepo = userRepo;
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
        Optional<ProviderAccount> existing = providerAccountRepo.findByProviderAndProviderUserId(provider.getId(), providerUserId);
        User owner;

        if (existing.isPresent()) {
            ProviderAccount acct = existing.get();
            acct.setAccessToken(accessToken);
            acct.setRefreshToken(refreshToken);
            acct.setExpiresAt(expiresAt);
            return providerAccountRepo.save(acct);
        }

        // if userid != null, means the user was passed in from token and is authenticated already
        if (userId != null) {
            owner = userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        } else {
            // No userId passed and no existing account -> create a minimal user record
            owner = new User();
            owner.setUsername(provider.getName() + ":" + providerUserId);
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

    private Provider resolveProvider(String providerName) {
        // Try numeric id first
        try {
            short id = Short.parseShort(providerName);
            return providerRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Provider not found by id: " + providerName));
        } catch (NumberFormatException ignore) {
            // Fallback to name
            return providerRepo.findByName(providerName)
                    .orElseThrow(() -> new IllegalArgumentException("Provider not found by name: " + providerName));
        }
    }
}