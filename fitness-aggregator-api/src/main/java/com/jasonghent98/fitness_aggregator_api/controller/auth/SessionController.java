package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.MagicLinkRequest;
import com.jasonghent98.fitness_aggregator_api.model.EmailVerification;
import com.jasonghent98.fitness_aggregator_api.model.User;
import com.jasonghent98.fitness_aggregator_api.repository.EmailVerificationRepository;
import com.jasonghent98.fitness_aggregator_api.repository.UserRepository;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.auth.EmailVerificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
    private final EmailVerificationRepository evRepo;
    private final UserRepository userRepo;

    public SessionController(
            JwtService jwtService,
            EmailVerificationService emailVeriService,
            FrontendConfig frontendConfig,
            EmailVerificationRepository evRepo,
            UserRepository userRepo
    ) {
        this.jwtService = jwtService;
        this.evService = emailVeriService;
        this.frontendConfig = frontendConfig;
        this.evRepo = evRepo;
        this.userRepo = userRepo;
    }

    // server-side authentication check (all jwts that make it passed the interceptor make it here)
    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        UUID id = UserContext.getUserId();
        System.out.println(id + "from SessionController.java");
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
        String link = frontendConfig.getFrontendOrigin() + "/api/auth/magic/verify?token=" + URLEncoder.encode(result.token(), StandardCharsets.UTF_8);
        System.out.println(link +  " FROM SESSIONCONTROLLER.JAVA");

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
                "sent", true,
                "link", link
        ));
    }

    /**
     * GET /api/auth/magic/verify?token=...
     * Verifies email token, creates/loads user, deletes verification row,
     * mints session JWT, and redirects to frontend to set cookie.
     */
    @GetMapping("/magic/verify")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {
        // 1) Decode the email token -> email
        Optional<String> emailOpt = jwtService.verifyEmailVerification(token); // implement this to return Optional<String> email
        if (emailOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid or expired link.");
        }
        String email = emailOpt.get();

        // 2) Ensure there is a matching, unexpired record (defense-in-depth)
        EmailVerification ev = evRepo.findByEmail(email)
                .orElse(null);
        if (ev == null || !token.equals(ev.getAccessToken()) || ev.getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.status(401).body("Link no longer valid. Please request a new one.");
        }

        // 3) Upsert user (create if not exists)
        User user = userRepo.findByEmailIgnoreCase(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            return userRepo.save(u);
        });

        // 4) One-time use: remove the verification row
        evRepo.deleteByEmail(email);

        // 5) Mint a session JWT (subject = userId) for app auth
        String sessionJwt = jwtService.mintSession(user.getId());

        // 6) Redirect to frontend, pass token so Next.js can set cookie via /api/session/set
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