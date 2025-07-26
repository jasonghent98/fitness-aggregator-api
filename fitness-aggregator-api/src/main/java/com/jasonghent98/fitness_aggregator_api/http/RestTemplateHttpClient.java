package com.jasonghent98.fitness_aggregator_api.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

/*concrete implementation of the HttpClient inteface: what will be used as our http library for now */
public class RestTemplateHttpClient implements HttpClient {
    private final RestTemplate restTemplate;

    public RestTemplateHttpClient() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public <T> T get(String url, Class<T> responseType, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );
        return response.getBody();
    }
}
