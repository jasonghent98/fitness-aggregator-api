package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.FrontendConfig;
import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.MagicLinkRequest;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.auth.EmailVerificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static com.jasonghent98.fitness_aggregator_api.util.auth.EmailVerificationUtil.looksLikeEmail;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final JwtService jwtService;
    private final EmailVerificationService emailVeriService;
    private final FrontendConfig frontendConfig;

    public SessionController(
            JwtService jwtService,
            EmailVerificationService emailVeriService,
            FrontendConfig frontendConfig
    ) {
        this.jwtService = jwtService;
        this.emailVeriService = emailVeriService;
        this.frontendConfig = frontendConfig;
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
        EmailVerificationService.IssueResult result = emailVeriService.issueOrRefresh(email);

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
                "sent", true
        ));
    }

    // verifies the token in the incoming req against the email_verifications for corresp email, then creates user and reroutes /connect-providers
    @GetMapping("/magic/verify")
    public ResponseEntity<?> verifyToken() {
        return ResponseEntity.ok().build();
    }

}