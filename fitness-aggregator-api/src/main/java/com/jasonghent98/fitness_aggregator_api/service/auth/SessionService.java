package com.jasonghent98.fitness_aggregator_api.service.auth;

import com.jasonghent98.fitness_aggregator_api.config.BackendConfig;
import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.model.auth.UserSession;
import com.jasonghent98.fitness_aggregator_api.repository.auth.UserSessionRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.EmailService;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SessionService {

    private final UserSessionRepository sessionRepo;
    private final UserService userService;
    private final JwtService jwtService;
    private final EmailVerificationService evService;
    private final FrontendConfig frontendConfig;
    private final BackendConfig backendConfig;
    private final EmailService emailService;

    public SessionService(
            UserSessionRepository sessionRepo,
            UserService userService,
            JwtService jwtService,
            EmailVerificationService evService,
            FrontendConfig frontendConfig,
            BackendConfig backendConfig,
            EmailService emailService
    ) {
        this.userService = userService;
        this.sessionRepo = sessionRepo;
        this.jwtService = jwtService;
        this.evService = evService;
        this.backendConfig = backendConfig;
        this.frontendConfig = frontendConfig;
        this.emailService = emailService;
    }


    // -----------------------
    // Public orchestration APIs
    // -----------------------

    /** Returns user metadata */
    public Map<String, Object> me(UUID userId) {
        if (userId == null) {
            return Map.of("authenticated", false);
        }
        // get the user metadata (tier) - guaranteed non-null by findTierForUser
        String subTier = userService.findTierForUser(userId);
        // Use explicit null check as extra safety for Map.of()
        String safeTier = (subTier != null) ? subTier : "FREE";
        return Map.of("authenticated", true, "userId", userId.toString(), "subscriptionTier", safeTier);
    }

    /** Logout and revoke session by refresh token */
    @Transactional
    public ResponseEntity<?> logout(String refreshToken) {
        sessionRepo.findByRefreshTokenAndRevokedAtIsNull(refreshToken)
                .ifPresent(session -> sessionRepo.delete(session));

        return ResponseEntity.ok(Map.of("ok", true));
    }

    /** Send magic link email */
    public ResponseEntity<?> sendMagicLink(String email) {
        var result = evService.issueOrRefresh(email);

        // reroute through Next.js proxy
        String link = frontendConfig.getFrontendOrigin() +
                "/api/email/verify-email?token=" +
                URLEncoder.encode(result.token(), StandardCharsets.UTF_8);

        // Queue email asynchronously - returns immediately
        emailService.sendEmail(
                email,
                "Your magic link to log in to Actualize",
                "<html>\n" +
                        "  <body style=\"font-family: Arial, sans-serif; color: #333;\">\n" +
                        "    <h2>Verify your email to log in</h2>\n" +
                        "    <p>Hi there,</p>\n" +
                        "    <p>Click the button below to verify your email and log in to <b>Actualize</b>:</p>\n" +
                        "    <p style=\"text-align: center;\">\n" +
                        "      <a href=\"" + link + "\"\n" +
                        "         style=\"background-color: #2563eb; color: white; padding: 12px 20px; \n" +
                        "                text-decoration: none; border-radius: 6px; font-weight: bold;\">\n" +
                        "        Log in to Actualize\n" +
                        "      </a>\n" +
                        "    </p>\n" +
                        "    <p>Or copy and paste this link into your browser:<br>\n" +
                        "      <a href=\"" + link + "\">" + link + "</a></p>\n" +
                        "    <p style=\"font-size: 12px; color: #666;\">\n" +
                        "      This link will expire in 10 minutes. If you didn't request this, you can safely ignore this email.\n" +
                        "    </p>\n" +
                        "    <p>– The Actualize Team</p>\n" +
                        "  </body>\n" +
                        "</html>"
        );

        log.info("Magic link email queued for {}", email);

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "sent", true
        ));
    }

    /** Verify magic link token */
    @Transactional
    public ResponseEntity<?> verifyMagicToken(String token, HttpServletRequest req) {
        String email = jwtService.verifyEmailVerification(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Upsert user
        User user = userService.upsertByEmail(email);

        // Create session
        String refreshToken = createSessionAndReturnRefreshToken(user.getId());

        // Get user's subscription tier
        String tier = userService.findTierForUser(user.getId());

        // Mint short-lived access JWT
        String accessJwt = jwtService.mintSession(user.getId(), tier);

        // Redirect (check if local testing)
        String next = frontendConfig.getFrontendOrigin() + "/app/onboarding/connect?status=verified";
        String host = req.getServerName(); // e.g. "localhost", "api.dev.actualize.fit"
        boolean isLocal = host.equals("localhost") || host.equals("127.0.0.1");

        return ResponseEntity.status(303)
                .header(HttpHeaders.LOCATION, next)
                // short lived 15 min jwt token
                .header(HttpHeaders.SET_COOKIE, jwtService.buildSessionCookie(accessJwt, isLocal))
                // refresh token cookie (longer-lived ~90 day token)
                .header(HttpHeaders.SET_COOKIE, jwtService.buildRefreshCookie(refreshToken, isLocal))
                .build();
    }

    /** Refresh access token */
    public ResponseEntity<?> refresh(String refreshToken) {
        return findValidSessionByRefreshToken(refreshToken)
                .map(session -> {
                    UUID userId = session.getUserId();
                    String tier = userService.findTierForUser(userId);
                    String newAccessToken = jwtService.mintSession(userId, tier);
                    String newRefreshToken = rotateRefreshToken(session);
                    return ResponseEntity.ok(Map.of(
                            "accessToken", newAccessToken,
                            "refreshToken", newRefreshToken
                    ));
                })
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Invalid or expired refresh token"))
                );
    }

    /**
     * Creates a new session row for a user, with refresh token and expiry.
     * If you want to allow multiple sessions per user (multi-device), this just inserts.
     * If you want only one session at a time, it replaces existing.
     */
    @Transactional
    public String createSessionAndReturnRefreshToken(UUID userId) {

       Map<String, Object> data = jwtService.mintRefresh(userId);

        // 💡 Choice: allow multiple sessions or force single session
        // If single-session, delete old ones:
        // sessionRepo.deleteByUserId(userId);

        UserSession session = UserSession.builder()
                .userId(userId)
                .refreshToken(data.get("token").toString())
                .refreshTokenExpiresAt((Instant) data.get("expiresAt"))
                .build();

        sessionRepo.save(session);
        return data.get("token").toString();
    }

    /**
     * Validate a refresh token and return the session if still valid.
     */
    public Optional<UserSession> findValidSessionByRefreshToken(String refreshToken) {
        log.debug("Looking up refresh token: {}...", refreshToken.substring(0, Math.min(8, refreshToken.length())));

        Optional<UserSession> session = sessionRepo.findByRefreshTokenAndRevokedAtIsNull(refreshToken);

        if (session.isEmpty()) {
            log.debug("No session found for refresh token");
            return Optional.empty();
        }

        if (session.get().getRefreshTokenExpiresAt().isBefore(Instant.now())) {
            log.debug("Refresh token found but expired at {}", session.get().getRefreshTokenExpiresAt());
            return Optional.empty();
        }

        log.debug("Valid session found for user {}", session.get().getUserId());
        return session;
    }

    /**
     * Deletes a session (e.g., logout).
     */
    @Transactional
    public void revokeSession(UUID userId, String refreshToken) {
        sessionRepo.findByUserIdAndRefreshToken(userId, refreshToken)
                .ifPresent(sessionRepo::delete);
    }

    /**
     * Rotates the refresh token for an existing session.
     * Invalidates the old token and issues a new one with a fresh expiry.
     */
    @Transactional
    public String rotateRefreshToken(UserSession session) {
        String oldToken = session.getRefreshToken();
        String newToken = UUID.randomUUID().toString();
        Instant newExpiry = Instant.now().plus(Duration.ofDays(30));

        log.info("Rotating refresh token for user {} - old: {}..., new: {}...",
                session.getUserId(),
                oldToken.substring(0, Math.min(8, oldToken.length())),
                newToken.substring(0, Math.min(8, newToken.length())));

        session.setRefreshToken(newToken);
        session.setRefreshTokenExpiresAt(newExpiry);
        session.setRevokedAt(null); // in case it was marked revoked

        sessionRepo.save(session);
        return newToken;
    }
}