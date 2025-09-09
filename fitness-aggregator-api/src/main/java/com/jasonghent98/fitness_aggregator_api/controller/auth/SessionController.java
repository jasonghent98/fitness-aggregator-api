package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final JwtService jwtService;

    public SessionController(JwtService jwtService) {
        this.jwtService = jwtService;
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
    public ResponseEntity<?> verifyEmail() {
        return ResponseEntity.ok().build();
    }

    // verifies the token in the incoming req against the email_verifications for corresp email, then creates user and reroutes /connect-providers
    @GetMapping("/magic/verify")
    public ResponseEntity<?> verifyToken() {
        return ResponseEntity.ok().build();
    }

}