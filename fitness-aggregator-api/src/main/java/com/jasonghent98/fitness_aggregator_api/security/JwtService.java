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
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    public static final String COOKIE_NAME = "ACTUALIZE_SESSION";

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

    public String mint(UUID userId) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofDays(cfg.getTtlDays()));
        return JWT.create()
                .withIssuer(cfg.getIssuer())
                .withSubject(userId.toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(alg);
    }

    public Optional<UUID> verify(String token) {
        try {
            DecodedJWT jwt = JWT.require(alg)
                    .withIssuer(cfg.getIssuer())   // must match what you used in mint()
                    .acceptLeeway(60)              // tolerate small clock skew
                    .build()
                    .verify(token);

            String sub = jwt.getSubject();
            UUID userId = UUID.fromString(sub); // will throw if not a UUID
            return Optional.of(userId);

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

    public String buildSessionCookie(String jwt) {
        // HttpOnly, Secure, SameSite=None; path "/" so all routes send it
        String rc = ResponseCookie.from(COOKIE_NAME, jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain(".actualize.fit") // client is handling the storing of jwt via same-site req for now
                .maxAge(Duration.ofDays(cfg.getTtlDays()))
                .build()
                .toString();
        return rc + "; Partitioned";
    }

    public String buildClearCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain(".actualize.fit") // client is handling the storing of jwt via same-site req for now
                .maxAge(Duration.ZERO)
                .build()
                .toString();
    }

    public String headerName() {
        return HttpHeaders.SET_COOKIE;
    }
}