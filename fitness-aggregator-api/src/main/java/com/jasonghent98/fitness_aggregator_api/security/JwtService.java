package com.jasonghent98.fitness_aggregator_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasonghent98.fitness_aggregator_api.config.JwtConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    public static final String COOKIE_NAME = "ACTUALIZE_SESSION";

    private final JwtConfig cfg;
    private final Algorithm alg;

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

    public UUID verify(String token) {
        DecodedJWT jwt = JWT.require(alg)
                .withIssuer(cfg.getIssuer())
                .build()
                .verify(token);
        return UUID.fromString(jwt.getSubject());
    }

    public String buildSessionCookie(String jwt) {
        // HttpOnly, Secure, SameSite=None; path "/" so all routes send it
        String rc = ResponseCookie.from(COOKIE_NAME, jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain("actualize.fit")
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