package com.jasonghent98.fitness_aggregator_api.interceptor.strava;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.service.strava.StravaAuthService;
import com.jasonghent98.fitness_aggregator_api.context.strava.StravaContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.UUID;


@Component
public class StravaTokenInterceptor implements HandlerInterceptor {

    StravaAuthService stravaAuthService;

    public StravaTokenInterceptor(StravaAuthService stravaAuthService) {
        this.stravaAuthService = stravaAuthService;
    }

    /*catches incoming requests and runs BEFORE the controller*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdHeader = request.getHeader("X-User-Id");


        if (userIdHeader == null) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing user ID");
            } catch (IOException e) {
                System.err.println("Unable to throw user ID error from StravaTokenInterceptor " + e);
            }

            return false;
        }


        UUID userId;
        // convert to UUID
        try {
            userId = UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
            } catch (IOException ioException) {
                System.err.println("Failed to send error response for invalid UUID: " + ioException);
            }
            return false;
        }

        // add to userid to UserContext for downstream access
        UserContext.setUserId(userId);

        String accessToken = stravaAuthService.getValidAccessToken(userId); // get or set the strava access token if expired in DB
        StravaContext.setAccessToken(accessToken); // set the token in memory for any other methods that need quick access within thread

        return true;
    }

    /*catches incoming requests and runs AFTER the controller*/

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StravaContext.clear(); // Clean up thread memory after the request to avoid future users with memory leak issues
    }
}