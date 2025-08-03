package com.jasonghent98.fitness_aggregator_api.interceptor.strava;

import com.jasonghent98.fitness_aggregator_api.context.strava.StravaContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class StravaTokenInterceptor implements HandlerInterceptor {



    public StravaTokenInterceptor() {

    }

    @Override
    /*
        should consider the following cases
        - if the user has an expired token, should handle the token refresh process automatically for strava-related routes (excluding initial login)
        - user has no token

     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    /*
     * Consider any memory leaks or cleanup that may need to be handled after the controller executes
     * */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StravaContext.clear(); // Clean up after the request
    }
}