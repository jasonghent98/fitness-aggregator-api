package com.jasonghent98.fitness_aggregator_api.config.provider.fitbit;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FitbitConfig {

    @Value("${fitbit.CLIENT_ID}")
    private String clientId;

    @Value("${fitbit.CLIENT_SECRET}")
    private String clientSecret;

    @Value("${fitbit.REDIRECT_URL}")
    private String redirectUrl;

}
