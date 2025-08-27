package com.jasonghent98.fitness_aggregator_api.config.provider.fitbit;

import com.jasonghent98.fitness_aggregator_api.interceptor.fitbit.FitbitTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class FitbitInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private FitbitTokenInterceptor fitbitTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(fitbitTokenInterceptor)
                .addPathPatterns("/api/fitbit/**")
                .excludePathPatterns("/api/fitbit/auth/**"); // Skip login and callback
    }
}