package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.config.MailerConfig;
import com.jasonghent98.fitness_aggregator_api.config.provider.strava.StravaConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.strava.StravaAuthTokenResponse;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/strava/auth")
public class StravaAuthController {
    public final StravaConfig stravaConfig;
    private final ProviderAccountRepository providerAccountRepo;
    private final ProviderRepository providerRepo;
    private final ProviderAccountService providerAccountService;
    private final FrontendConfig frontendConfig;
    private final JwtService jwtService;
    private final MailerConfig mailerConfig;

    /*spring will recognize this is a bean and will handle instantiation and injection*/
    StravaAuthController(
            StravaConfig stravaConfig,
            FrontendConfig frontendConfig,
            ProviderAccountRepository providerAccountRepo,
            ProviderRepository providerRepo,
            ProviderAccountService providerAccountService,
            JwtService jwtService,
            MailerConfig mailerConfig
    ) {
        this.stravaConfig = stravaConfig;
        this.providerAccountRepo = providerAccountRepo;
        this.providerRepo = providerRepo;
        this.providerAccountService = providerAccountService;
        this.frontendConfig = frontendConfig;
        this.jwtService = jwtService;
        this.mailerConfig = mailerConfig;
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

        // test email runner
        mailerConfig.sendSimple("jasonghent1008@gmail.com", "test ses", "test run from actualize StravaAuthController.java");

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
            String accessToken = tokenData.getBody().accessToken;
            String refreshToken = tokenData.getBody().refreshToken;
            Instant expiresAt = Instant.ofEpochSecond(tokenData.getBody().expiresAt);


            // get the associated strava user if exists
            Optional<Provider> stravaProvider = providerRepo.findByName("strava");
            Optional<ProviderAccount> existingStravaUserOpt = providerAccountRepo.findByProviderAndProviderUserId(stravaProvider.get(), stravaAthleteId.toString());
            UUID userId;
            if (existingStravaUserOpt.isPresent()) {
                userId = existingStravaUserOpt.get().getUser().getId();
            } else if (UserContext.getUserId() != null) {
                userId = UserContext.getUserId();
            } else {
                userId = null;
            }

            // upsert the strava user (pass in the user id from the token if exists)
            ProviderAccount new_acc = providerAccountService.upsertProviderAccount(userId, "strava", stravaAthleteId.toString(), accessToken, refreshToken, expiresAt);


            // mint/create session JWT
            String jwt = jwtService.mint(new_acc.getUser().getId());

            // append token as query param
            URI redirect = URI.create(frontendConfig.getFrontendOrigin()
                    + "/get-started?provider=strava&status=success&token="
                    + URLEncoder.encode(jwt, StandardCharsets.UTF_8));

            return ResponseEntity.status(302)
                    .location(redirect)
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .build();


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to exchange token: " + e.getMessage());
        }
    }
}
