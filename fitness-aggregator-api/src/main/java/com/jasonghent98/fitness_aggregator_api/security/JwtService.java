package com.jasonghent98.fitness_aggregator_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasonghent98.fitness_aggregator_api.config.JwtConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    public static final String SESSION_COOKIE_NAME = "ACTUALIZE_SESSION";
    public static final String REFRESH_COOKIE_NAME = "ACTUALIZE_REFRESH";
    public static final String DOMAIN_FOR_COOKIE = ".actualize.fit";

    private final JwtConfig cfg;
    private final Algorithm alg;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public JwtService(JwtConfig cfg) {
        this.cfg = cfg;
        if (cfg.getSecret() == null || cfg.getSecret().isBlank()) {
            throw new IllegalStateException("JWT_SECRET is required");
        }
        this.alg = Algorithm.HMAC256(cfg.getSecret());
    }

    public String mintSession(UUID userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(cfg.getSessionTtlMinutes()));
        return JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(userId.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(alg);
    }

    public Map<String, Object> mintRefresh(UUID userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofDays(cfg.getRefreshTtlDays()));
        String token = JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(userId.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(alg);
        return Map.of(
                "token", token,
                "expiresAt", exp
        );
    }

    public Optional<SessionInfo> verifySession(String token) {
        try {
            DecodedJWT jwt = JWT.require(alg)
                    .withIssuer(cfg.getIssuer())
                    .acceptLeeway(60)
                    .build()
                    .verify(token);

            String sub = jwt.getSubject();
            UUID userId = UUID.fromString(sub);

            String tier = jwt.getClaim("tier").asString();

            return Optional.of(new SessionInfo(userId, tier));

        } catch (TokenExpiredException e) {
            log.warn("JWT expired at {}", e.getExpiredOn());
        } catch (InvalidClaimException e) {
            log.warn("Invalid claim: {}", e.getMessage());
        } catch (SignatureVerificationException e) {
            log.warn("Bad signature (wrong secret/alg)");
        } catch (AlgorithmMismatchException e) {
            log.warn("Algorithm mismatch");
        } catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT subject is not a UUID");
        }
        return Optional.empty();
    }

    /** Builds the session cookie for frontend auth */
    public String buildSessionCookie(String jwt, Boolean isLocal) {
        // HttpOnly, Secure, SameSite=None; path "/" so all routes send it
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(SESSION_COOKIE_NAME, jwt)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .maxAge(Duration.ofMinutes(cfg.getSessionTtlMinutes())); // short-lived token (~15 mins)
        if (isLocal) {
            cookieBuilder.secure(false);
            // Set domain to localhost (without port) so cookie works across frontend:3000 and backend:8080
            cookieBuilder.domain("localhost");
        } else {
            cookieBuilder.secure(true);
            cookieBuilder.domain(DOMAIN_FOR_COOKIE);
        }

        return cookieBuilder.build().toString();
    }

    /** Builds the refresh cookie for silent user auth persistence */
    public String buildRefreshCookie(String refreshToken, Boolean isLocal) {

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .maxAge(Duration.ofDays(cfg.getRefreshTtlDays()));  // long-lived token (~60 days)
        if (isLocal) {
            cookieBuilder.secure(false);
            // Set domain to localhost (without port) so cookie works across frontend:3000 and backend:8080
            cookieBuilder.domain("localhost");
        } else {
            cookieBuilder.secure(true);
            cookieBuilder.domain(DOMAIN_FOR_COOKIE);
        }

        return cookieBuilder.build().toString();
    }

    public String buildClearCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain(DOMAIN_FOR_COOKIE) // client is handling the storing of jwt via same-site req for now
                .maxAge(Duration.ZERO)
                .build()
                .toString();
    }

    public String mintEmailVerification(String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(15));
        return JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(email)
                .withClaim("kind", "email-verify") // disambiguate from session JWTs
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(alg);
    }

    // returns the email if valid
    public Optional<String> verifyEmailVerification(String token) {
        try {
            DecodedJWT jwt = JWT.require(alg)
                    .withIssuer(cfg.getIssuer())
                    .withClaim("kind", "email-verify")
                    .acceptLeeway(60)
                    .build()
                    .verify(token);

            String email = jwt.getSubject();
            if (email == null || email.isBlank()) return Optional.empty();
            return Optional.of(email);
        } catch (TokenExpiredException e) {
            log.warn("Email verify token expired at {}", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            log.warn("Email verify token invalid: {}", e.getMessage());
        }
        return Optional.empty();
    }

    public String headerName() {
        return HttpHeaders.SET_COOKIE;
    }

    // small dto for verifyToken
    public record SessionInfo(UUID userId, String tier) {}
}