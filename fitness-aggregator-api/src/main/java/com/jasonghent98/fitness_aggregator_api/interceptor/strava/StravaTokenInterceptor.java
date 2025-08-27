// src/main/java/.../interceptor/strava/StravaTokenInterceptor.java
package com.jasonghent98.fitness_aggregator_api.interceptor.strava;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.strava.StravaContext;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.UUID;

@Component
public class StravaTokenInterceptor implements HandlerInterceptor {

    private final ProviderAccountService providerAccountService;

    public StravaTokenInterceptor(ProviderAccountService providerAccountService) {
        this.providerAccountService = providerAccountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        UUID userId = UserContext.getUserId();
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authenticated");
            return false;
        }
        String accessToken = providerAccountService.getValidAccessToken(userId, "strava");
        StravaContext.setAccessToken(accessToken);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StravaContext.clear();
        UserContext.clear();
    }
}