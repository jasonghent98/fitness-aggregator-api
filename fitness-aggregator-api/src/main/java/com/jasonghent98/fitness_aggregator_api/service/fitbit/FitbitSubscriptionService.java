package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class FitbitSubscriptionService {

    private final RestTemplate restTemplate;

    @Value("${fitbit.api.base:https://api.fitbit.com}")
    private String fitbitApiBase;

    // Optional: only needed if you configured multiple subscribers in the Fitbit Dev portal
    @Value("${fitbit.subscriber.id:}")
    private String subscriberId;

    public FitbitSubscriptionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public enum Collection {
        ACTIVITIES("activities"),
        SLEEP("sleep"),
        BODY("body"),
        FOODS("foods");

        public final String path;
        Collection(String path) { this.path = path; }
    }

    /**
     * Create (idempotent) Fitbit subscription for the authorized user.
     * POST /1/user/-/{collection}/apiSubscriptions/{subscriptionId}.json
     *
     * @param accessToken   OAuth access token for the Fitbit user
     * @param collection    e.g., Collection.ACTIVITIES, Collection.SLEEP
     * @param subscriptionId any stable id you choose per user+collection (e.g. userId+"-act")
     * @return true if created or already existed; false only on unexpected non-2xx/409
     */
    public boolean createSubscription(String accessToken, Collection collection, String subscriptionId) {
        String encodedId = URLEncoder.encode(subscriptionId, StandardCharsets.UTF_8);
        String url = String.format("%s/1/user/-/%s/apiSubscriptions/%s.json",
                fitbitApiBase, collection.path, encodedId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        if (subscriberId != null && !subscriberId.isBlank()) {
            headers.set("X-Fitbit-Subscriber-Id", subscriberId);
        }

        try {
            ResponseEntity<String> resp = restTemplate.exchange(
                    url, HttpMethod.POST, new HttpEntity<>(headers), String.class);
            // Fitbit typically returns 201 Created on success
            return resp.getStatusCode().is2xxSuccessful() || resp.getStatusCode() == HttpStatus.CREATED;
        } catch (HttpClientErrorException e) {
            // 409 = already subscribed → treat as success (idempotent)
            if (e.getStatusCode() == HttpStatus.CONFLICT) return true;
            // 401/403/etc → surface for caller to handle (e.g., refresh token)
            System.out.println("Error creating subscription for accessToken: " + accessToken + " collection: " + collection);
            throw e;
        } catch (org.springframework.web.client.ResourceAccessException e) { // IO/timeout
            System.out.println("Fitbit subscription network error: " + collection + " " + subscriptionId + " " + e.getMessage());
            throw e; // bubble up
        }
    }

    // Convenience: call for your defaults right after OAuth token is stored
    public void createDefaultSubscriptions(String accessToken, String userKey) {

        for (var c : List.of(Collection.ACTIVITIES, Collection.BODY, Collection.FOODS, Collection.SLEEP)) {
            String subId = userKey + "-" + c.name().toLowerCase();
            try {
                createSubscription(accessToken, c, subId); // may throw
            } catch (Exception e) {
                // catch/swallow the error ; let the loop continue
            }
        }

    }
}