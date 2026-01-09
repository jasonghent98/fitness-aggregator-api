package com.jasonghent98.fitness_aggregator_api.security;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.model.auth.UserSession;
import com.jasonghent98.fitness_aggregator_api.service.auth.SessionService;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(JwtSessionFilter.class);
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserService userService;

    public JwtSessionFilter(JwtService jwtService, SessionService sessionService, UserService userService) {
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.userService = userService;
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
            Optional<JwtService.SessionInfo> sessionInfo = Optional.empty();
            if (token != null && !token.isBlank()) {
                sessionInfo = jwtService.verifySession(token);

                if (sessionInfo.isPresent()) {
                    UserContext.setUserId(sessionInfo.get().userId());
                    UserContext.setTier(sessionInfo.get().tier());
                } else {
                    // Token is expired or invalid - try to refresh silently
                    log.info("Access token expired or invalid, attempting silent refresh");
                    attemptSilentRefresh(req, res);
                }
            } else {
                // No access token - try to refresh silently if refresh token exists
                attemptSilentRefresh(req, res);
            }

            chain.doFilter(req, res);
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Attempt to refresh the access token using the refresh token from cookies
     */
    private void attemptSilentRefresh(HttpServletRequest req, HttpServletResponse res) {
        try {
            // Get refresh token from cookies
            String refreshToken = Optional.ofNullable(req.getCookies())
                    .flatMap(cookies -> Arrays.stream(cookies)
                            .filter(c -> JwtService.REFRESH_COOKIE_NAME.equals(c.getName()))
                            .findFirst())
                    .map(Cookie::getValue)
                    .orElse(null);

            if (refreshToken == null || refreshToken.isBlank()) {
                log.debug("No refresh token found in cookies");
                return;
            }

            log.debug("Attempting silent refresh with token: {}...", refreshToken.substring(0, Math.min(8, refreshToken.length())));

            // Validate refresh token and get session
            Optional<UserSession> validSession = sessionService.findValidSessionByRefreshToken(refreshToken);

            if (validSession.isPresent()) {
                    UserSession session = validSession.get();
                    UUID userId = session.getUserId();

                    // Get user's subscription tier
                    String tier = userService.findTierForUser(userId);

                    // Mint new access token with tier
                    String newAccessToken = jwtService.mintSession(userId, tier);

                    // Rotate refresh token for security
                    String newRefreshToken = sessionService.rotateRefreshToken(session);

                    // Determine if local environment
                    String host = req.getServerName();
                    boolean isLocal = host.equals("localhost") || host.equals("127.0.0.1");

                    // Set new cookies in response
                    res.addHeader("Set-Cookie", jwtService.buildSessionCookie(newAccessToken, isLocal));
                    res.addHeader("Set-Cookie", jwtService.buildRefreshCookie(newRefreshToken, isLocal));

                    // Set user context for this request
                    UserContext.setUserId(userId);
                    UserContext.setTier(tier);

                    log.info("Silent token refresh successful for user {} with new tokens", userId);
                } else {
                    log.warn("Refresh token not found or expired - token: {}...",
                            refreshToken != null ? refreshToken.substring(0, Math.min(8, refreshToken.length())) : "null");
                    log.warn("User needs to re-authenticate");
                }
        } catch (Exception e) {
            log.error("Silent refresh failed with exception: {}", e.getMessage(), e);
            // Fail silently - user will remain unauthenticated
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip CORS preflight and anything else you want to allow through unconditionally
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        // Skip third-party webhook endpoints (they never send your JWT)

        if (path.startsWith("/api/fitbit/webhook") ||
                path.startsWith("/api/oura/webhook") ||
                path.startsWith("/api/garmin/webhook")) {
            return true;
        }

        return false;
    }
}