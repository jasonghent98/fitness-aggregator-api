package com.jasonghent98.fitness_aggregator_api.security;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(10)
public class JwtSessionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtSessionFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {

        try {
            // 1) Prefer our private header (set by Next.js server route)
            String token = req.getHeader("X-Actualize-Session");

            // 2) Fallback to jwt cookie (direct browser → backend calls)
            if (token == null || token.isBlank()) {
                token = Optional.ofNullable(req.getCookies())
                        .flatMap(cookies -> Arrays.stream(cookies)
                                .filter(c -> JwtService.SESSION_COOKIE_NAME.equals(c.getName()))
                                .findFirst())
                        .map(Cookie::getValue)
                        .orElse(null);
            }

            // 3) Verify if present
            if (token != null && !token.isBlank()) {
                try {

                    jwtService.verifySession(token).ifPresent(session -> {
                        UserContext.setUserId(session.userId());
                        UserContext.setTier(session.tier());
                    });

                } catch (Exception verifyErr) {
                    // Invalid/expired -> just proceed unauthenticated; endpoint decides response
                }
            }

            chain.doFilter(req, res);
        } finally {
            UserContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip CORS preflight and anything else you want to allow through unconditionally
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}