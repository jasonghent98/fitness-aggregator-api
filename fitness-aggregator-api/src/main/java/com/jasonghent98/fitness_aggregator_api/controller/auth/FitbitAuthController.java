package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.fitbit.FitbitConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fitbit/auth")
public class FitbitAuthController {

    private final FitbitConfig fitbitConfig;
    private final FrontendConfig frontendConfig;
    private final ObjectMapper json = new ObjectMapper();
    private final ProviderAccountService providerAccountService;
    private final ProviderRepository providerRepo;
    private final ProviderAccountRepository providerAccountRepo;
    private final JwtService jwtService;

    public FitbitAuthController(
            FitbitConfig fitbitConfig,
            FrontendConfig frontendConfig,
            ProviderAccountService providerAccountService,
            ProviderRepository providerRepo,
            ProviderAccountRepository providerAccountRepo,
            JwtService jwtService
    ) {
        this.fitbitConfig = fitbitConfig;
        this.frontendConfig = frontendConfig;
        this.providerAccountService = providerAccountService;
        this.providerRepo = providerRepo;
        this.providerAccountRepo = providerAccountRepo;
        this.jwtService = jwtService;
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

            // unpack the variables from the strava oauth response
            String fitbitUserId = body.user_id; // Fitbit returns a string user id
            Instant expiresAt = Instant.now().plusSeconds(body.expires_in != null ? body.expires_in : 0);
            String accessToken = body.access_token;
            String refreshToken = body.refresh_token;

            // get the associated strava user if exists
            Optional<Provider> fitbitProvider = providerRepo.findByName("fitbit");
            Optional<ProviderAccount> existingFitbitUserOpt = providerAccountRepo.findByProviderAndProviderUserId(fitbitProvider.get(), fitbitUserId);
            UUID userId;
            if (existingFitbitUserOpt.isPresent()) {
                userId = existingFitbitUserOpt.get().getUser().getId();
            } else if (UserContext.getUserId() != null) {
                userId = UserContext.getUserId();
            } else {
                userId = null;
            }

            // upsert the fitbit user + base user (pass in the user id from the token if exists)
            ProviderAccount new_acc = providerAccountService.upsertProviderAccount(userId, "fitbit", fitbitUserId, accessToken, refreshToken, expiresAt);

            // mint/create session JWT
            String jwt = jwtService.mintSession(new_acc.getUser().getId());

            // append token as query param
            URI redirect = URI.create(frontendConfig.getFrontendOrigin()
                    + "/get-started?provider=fitbit&status=success&token="
                    + URLEncoder.encode(jwt, StandardCharsets.UTF_8));

            return ResponseEntity.status(302)
                    .location(redirect)
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Fitbit auth failed: " + e.getMessage());
        }
    }
}