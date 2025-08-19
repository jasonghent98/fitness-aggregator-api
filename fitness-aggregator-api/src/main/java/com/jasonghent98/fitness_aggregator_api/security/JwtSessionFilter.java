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

// the OncePerRequest filter class runs a filter for every incoming HTTP request
@Component
@Order(10)
public class JwtSessionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtSessionFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // finds the cookie issued from this server, verifies the userId and sets it via UserContext for global state access
    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        try {
            Optional<Cookie> cookie = Optional.ofNullable(req.getCookies())
                    .flatMap(cookies -> Arrays.stream(cookies)
                            .filter(c -> JwtService.COOKIE_NAME.equals(c.getName()))
                            .findFirst());

            if (cookie.isPresent()) {
                try {
                    UUID userId = jwtService.verify(cookie.get().getValue());
                    UserContext.setUserId(userId);
                } catch (Exception ignored) {
                    // invalid/expired token -> just proceed unauthenticated
                }
            }

            chain.doFilter(req, res);
        } finally {
            UserContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // If you want to skip static/assets, add exclusions here
        return false;
    }
}