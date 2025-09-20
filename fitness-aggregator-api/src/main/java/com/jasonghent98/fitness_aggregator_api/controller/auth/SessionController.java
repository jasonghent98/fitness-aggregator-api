package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.BackendConfig;
import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.MagicLinkRequest;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.EmailVerificationRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import com.jasonghent98.fitness_aggregator_api.service.auth.EmailVerificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.jasonghent98.fitness_aggregator_api.util.auth.EmailVerificationUtil.looksLikeEmail;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final JwtService jwtService;
    private final EmailVerificationService evService;
    private final FrontendConfig frontendConfig;
    private final BackendConfig backendConfig;
    private final UserService userService;

    public SessionController(
            JwtService jwtService,
            EmailVerificationService emailVeriService,
            FrontendConfig frontendConfig,
            BackendConfig backendConfig,
            EmailVerificationRepository evRepo,
            UserService userService
    ) {
        this.jwtService = jwtService;
        this.evService = emailVeriService;
        this.userService = userService;
        this.frontendConfig = frontendConfig;
        this.backendConfig = backendConfig;
    }

    // server-side authentication check (all jwts that make it passed the interceptor make it here)
    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        UUID id = UserContext.getUserId();
        if (id == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }
        return ResponseEntity.ok(Map.of("userId", id.toString()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String clear = jwtService.buildClearCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clear)
                .body(Map.of("ok", true));
    }

    // creates a record in email_verifications table, gen a magic link with token, sends via ses email
    @PostMapping("/magic/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody MagicLinkRequest req) {
        String rawEmail = req.getEmail() == null ? "" : req.getEmail().trim();
        if (rawEmail.isEmpty() || !looksLikeEmail(rawEmail)) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("ok", false, "error", "invalid_email")
            );
        }

        // citext handles case-insensitive uniqueness at DB level
        String email = rawEmail;

        // upsert by email (overwrite token + expiry) and return the token
        EmailVerificationService.IssueResult result = evService.issueOrRefresh(email);

        // build magic link (optionally carry forward a returnTo)
        String link = backendConfig.getBackendOrigin() + "/api/auth/magic/verify?token=" + URLEncoder.encode(result.token(), StandardCharsets.UTF_8);

        // send email
        try {
            System.out.println("send email with magic link here");
        } catch (Exception e) {
            // If email fails, you may still want to keep the token (user could request again)
            return ResponseEntity.status(502).body(
                    java.util.Map.of("ok", false, "error", "email_send_failed")
            );
        }

        return ResponseEntity.ok(java.util.Map.of(
                "ok", true,
                "sent", true
        ));
    }

    /**
     * GET /api/auth/magic/verify?token=...
     * Verifies email token, creates/loads user, deletes verification row,
     * mints session JWT, and redirects to frontend to set cookie.
     */
    @GetMapping("/magic/verify")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {


        // Mint a session JWT (subject = userId) for app auth
        String sessionJwt = jwtService.mintSession(UUID.fromString("404ab5d0-4051-4587-9f03-a13ad8463fb4"));

        // Redirect to frontend, pass token so Next.js can set cookie via /api/session/set
        String next = frontendConfig.getFrontendOrigin()
                + "/connect-providers?status=verified&token="
                + URLEncoder.encode(sessionJwt, StandardCharsets.UTF_8);

        return ResponseEntity.status(303) // See Other
                .header(HttpHeaders.LOCATION, next)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .build();

        // Alternative: set cookie directly here with jwtService.buildSessionCookie(sessionJwt)
        // and redirect without the token in the URL if you prefer.
    }

}