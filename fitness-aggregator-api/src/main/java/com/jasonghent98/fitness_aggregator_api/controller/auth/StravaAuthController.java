package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.services.StravaConfig;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/strava/auth")
public class StravaAuthController {
    public final StravaConfig stravaConfig;

    /*spring will recognize this is a bean and will handle instantiation and injection*/
    StravaAuthController (StravaConfig stravaConfig) {
        this.stravaConfig = stravaConfig;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToStrava() {
        String stravaClientId = stravaConfig.getClientId();
        String stravaRedirectURL = stravaConfig.getRedirectUrl();

        String url = "https://www.strava.com/oauth/authorize?" +
                "client_id=" + stravaClientId +
                "&redirect_uri=" + stravaRedirectURL +
                "&response_type=code" +
                "&scope=read,activity:read"; // adjust scopes
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        /*strava config variables*/
        String stravaClientId = stravaConfig.getClientId();
        String stravaClientSecret = stravaConfig.getClientSecret();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", stravaClientId);
        params.add("client_secret", stravaClientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://www.strava.com/oauth/token",
                    request,
                    String.class
            );

            // Normally you'd parse the response into an object and persist it.
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}
