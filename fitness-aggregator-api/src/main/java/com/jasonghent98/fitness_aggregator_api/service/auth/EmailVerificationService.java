package com.jasonghent98.fitness_aggregator_api.service.auth;

import com.jasonghent98.fitness_aggregator_api.model.EmailVerification;
import com.jasonghent98.fitness_aggregator_api.repository.EmailVerificationRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

import static com.jasonghent98.fitness_aggregator_api.util.auth.EmailVerificationUtil.normalizeEmail;

@Service
@Transactional
public class EmailVerificationService {

    private final EmailVerificationRepository repo;
    private final JwtService jwtService;
    private final Duration ttl;

    /**
     * TTL for magic-link tokens. Example: 15 minutes.
     * Configure with: auth.magic.ttl-minutes=15
     */
    public EmailVerificationService(
            EmailVerificationRepository repo,
            JwtService jwtService,
            @Value("${auth.magic.ttl-minutes:15}") long ttlMinutes
    ) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.ttl = Duration.ofMinutes(ttlMinutes);
    }

    /**
     * Create or refresh a verification challenge for the given email.
     * Generates a short-lived email-verification JWT and upserts the record.
     *
     * @return the upserted entity and the freshly minted token.
     */
    public IssueResult issueOrRefresh(String rawEmail) {
        String email = normalizeEmail(rawEmail);

        // mint a fresh email-verification token (expiry handled within method)
        String token = jwtService.mintEmailVerification(email);

        Instant expiresAt = Instant.now().plus(ttl);

        // upsert DB row for this email
        EmailVerification ev = repo.findByEmail(email)
                .map(existing -> {
                    existing.setAccessToken(token);
                    existing.setExpiresAt(expiresAt);
                    // updatedAt handled by @PreUpdate
                    return existing;
                })
                .orElseGet(() -> {
                    EmailVerification created = new EmailVerification();
                    created.setEmail(email);
                    created.setAccessToken(token);
                    created.setExpiresAt(expiresAt);
                    return created;
                });

        EmailVerification saved = repo.save(ev);
        return new IssueResult(saved, token, expiresAt);
    }

    /**
     * Verify an incoming magic-link token and consume it (delete the row).
     * - Verifies the JWT (signature, issuer, exp) and extracts the email.
     * - Confirms there is a current DB row for that email with the same token and not expired.
     * - Deletes the row to prevent reuse.
     *
     * @return the verified email if valid; empty otherwise.
     */
    public Optional<String> verifyAndConsume(String token) {
        // crypto verification + extract email
        Optional<String> emailOpt = jwtService.verifyEmailVerification(token);
        if (emailOpt.isEmpty()) return Optional.empty();

        // util static method
        String email = normalizeEmail(emailOpt.get());

        // match against DB row
        Optional<EmailVerification> rowOpt = repo.findByEmail(email);
        if (rowOpt.isEmpty()) return Optional.empty();
        EmailVerification row = rowOpt.get();

        boolean tokenMatches = token.equals(row.getAccessToken());
        boolean notExpired = row.getExpiresAt() != null && row.getExpiresAt().isAfter(Instant.now());

        if (!tokenMatches || !notExpired) {
            return Optional.empty();
        }

        // delete to prevent reuse
        repo.delete(row);

        return Optional.of(email);
    }



    /** Simple return type for issueOrRefresh */
    public record IssueResult(EmailVerification entity, String token, Instant expiresAt) {}
}