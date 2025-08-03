package com.jasonghent98.fitness_aggregator_api.config.provider.strava;

import com.jasonghent98.fitness_aggregator_api.interceptor.strava.StravaTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class StravaInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private StravaTokenInterceptor stravaTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stravaTokenInterceptor)
                .addPathPatterns("/api/strava/**")
                .excludePathPatterns("/api/strava/auth/**"); // Skip login and callback
    }
}