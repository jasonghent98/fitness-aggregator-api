package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.services.StravaConfig;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import com.jasonghent98.fitness_aggregator_api.repository.strava.StravaUserRepository;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/strava/auth")
public class StravaAuthController {
    public final StravaConfig stravaConfig;
    private final UserRepository userRepo;
    private final StravaUserRepository stravaUserRepo;

    /*spring will recognize this is a bean and will handle instantiation and injection*/
    StravaAuthController (StravaConfig stravaConfig, UserRepository userRepo, StravaUserRepository stravaUserRepo) {
        this.stravaConfig = stravaConfig;
        this.userRepo = userRepo;
        this.stravaUserRepo = stravaUserRepo;
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
            ResponseEntity<StravaAuthTokenResponse> response = restTemplate.postForEntity(
                    "https://www.strava.com/oauth/token",
                    request,
                    StravaAuthTokenResponse.class
            );

            ResponseEntity<StravaAuthTokenResponse> tokenData = response;

            if (tokenData == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empty response from Strava Authentication");
            }

            // unpack the variables from the strava oauth response
            Long stravaAthleteId = tokenData.getBody().athlete.id;
            String stravaUsername = tokenData.getBody().athlete.username;
            String stravaFirstName = tokenData.getBody().athlete.firstName;
            String stravaLastName = tokenData.getBody().athlete.lastName;
            Instant now = Instant.now();

            // get the associated strava user if exists
            Optional<StravaUser> existingStravaUserOpt = stravaUserRepo.findByStravaAthleteId(stravaAthleteId);

            // if strava user exists, just update the token; else, create new entry in both users and strava_users tables
            if (existingStravaUserOpt.isPresent()) {

                StravaUser existing = existingStravaUserOpt.get();
                existing.setAccessToken(tokenData.getBody().accessToken);
                existing.setRefreshToken(tokenData.getBody().refreshToken);
                existing.setExpiresAt(Instant.ofEpochSecond(tokenData.getBody().expiresAt));
                stravaUserRepo.save(existing);

            } else {

                // Create User
                User newUser = new User();
                newUser.setFullName(stravaFirstName + " " + stravaLastName);
                newUser.setUsername(stravaUsername);
                userRepo.save(newUser);

                // Create StravaUser
                StravaUser newStravaUser = new StravaUser();
                newStravaUser.setUser(newUser);
                newStravaUser.setStravaAthleteId(stravaAthleteId);
                newStravaUser.setAccessToken(tokenData.getBody().accessToken);
                newStravaUser.setRefreshToken(tokenData.getBody().refreshToken);
                newStravaUser.setExpiresAt(Instant.ofEpochSecond(tokenData.getBody().expiresAt));
                stravaUserRepo.save(newStravaUser);
            }

            return ResponseEntity.ok("Strava authentication successful");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}
