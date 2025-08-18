package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitUser;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import com.jasonghent98.fitness_aggregator_api.repository.fitbit.FitbitUserRepository;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/fitbit/auth")
public class FitbitAuthController {

    private final FitbitConfig fitbitConfig;
    private final FrontendConfig frontendConfig;
    private final UserRepository userRepo;
    private final FitbitUserRepository fitbitUserRepo;
    private final ObjectMapper json = new ObjectMapper();

    public FitbitAuthController(
            FitbitConfig fitbitConfig,
            FrontendConfig frontendConfig,
            UserRepository userRepo,
            FitbitUserRepository fitbitUserRepo
    ) {
        this.fitbitConfig = fitbitConfig;
        this.frontendConfig = frontendConfig;
        this.userRepo = userRepo;
        this.fitbitUserRepo = fitbitUserRepo;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToFitbit() {
        String url = "https://www.fitbit.com/oauth2/authorize" +
                "?client_id=" + fitbitConfig.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + fitbitConfig.getRedirectUrl() +
                "&scope=" + String.join("%20", new String[]{"activity","heartrate","sleep","profile"});
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        try {
            // 1) Exchange code for tokens
            RestTemplate rt = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            // Fitbit requires Basic auth: base64(client_id:client_secret)
            String basic = fitbitConfig.getClientId() + ":" + fitbitConfig.getClientSecret();
            String basicB64 = java.util.Base64.getEncoder().encodeToString(basic.getBytes());
            headers.set("Authorization", "Basic " + basicB64);

            MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
            form.add("client_id", fitbitConfig.getClientId());
            form.add("grant_type", "authorization_code");
            form.add("code", code);
            form.add("redirect_uri", fitbitConfig.getRedirectUrl());

            ResponseEntity<FitbitAuthTokenResponse> tokenResp = rt.postForEntity(
                    "https://api.fitbit.com/oauth2/token",
                    new HttpEntity<>(form, headers),
                    FitbitAuthTokenResponse.class
            );

            FitbitAuthTokenResponse body = tokenResp.getBody();
            if (body == null) {
                return ResponseEntity.status(500).body("Empty response from Fitbit token exchange");
            }

            // 2) Log entire response so you can see the shape (for now)
            System.out.println("Fitbit token response: " + json.writeValueAsString(body));

            // 3) Upsert FitbitUser + base User
            String fitbitUserId = body.user_id; // Fitbit returns a string user id
            Instant expiresAt = Instant.now().plusSeconds(body.expires_in != null ? body.expires_in : 0);

            Optional<FitbitUser> existingFitbit = fitbitUserRepo.findByFitbitUserId(fitbitUserId);
            if (existingFitbit.isPresent()) {
                FitbitUser fu = existingFitbit.get();
                fu.setAccessToken(body.access_token);
                fu.setRefreshToken(body.refresh_token);
                fu.setExpiresAt(expiresAt);
                fu.setScope(body.scope);
                fitbitUserRepo.save(fu);
            } else {
                // Create base User (minimal – adjust as you add profile fetch later)
                User u = new User();
                // If you don’t have an email/name yet, use a placeholder username
                u.setUsername("fitbit_" + fitbitUserId);
                userRepo.save(u);

                FitbitUser fu = new FitbitUser();
                fu.setUser(u);
                fu.setFitbitUserId(fitbitUserId);
                fu.setAccessToken(body.access_token);
                fu.setRefreshToken(body.refresh_token);
                fu.setExpiresAt(expiresAt);
                fu.setScope(body.scope);
                fitbitUserRepo.save(fu);
            }

            // 4) Redirect back to frontend with success flash
            URI redirect = URI.create(frontendConfig.getFrontendOrigin() + "/get-started?provider=fitbit&status=success");
            return ResponseEntity.status(303).location(redirect).build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Fitbit auth failed: " + e.getMessage());
        }
    }
}