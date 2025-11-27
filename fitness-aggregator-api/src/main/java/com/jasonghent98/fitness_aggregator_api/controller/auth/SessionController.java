package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.MagicLinkRequest;
import com.jasonghent98.fitness_aggregator_api.security.JwtService;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import com.jasonghent98.fitness_aggregator_api.service.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final SessionService sessionService;
    private JwtService jwtService;
    private UserService userService;

    public SessionController(SessionService sessionService, JwtService jwtService, UserService userService) {
        this.sessionService = sessionService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> whoami() {
        UUID userId = UserContext.getUserId();
        return ResponseEntity.ok(userService.getUser(userId));
    }
    /** Generate a valid JWT tied to a user for testing purposes: delete once in prod */
    @GetMapping("/generateToken")
    public ResponseEntity<?> getToken() {
        String token = jwtService.mintSession(UUID.fromString("404ab5d0-4051-4587-9f03-a13ad8463fb4"));
        return ResponseEntity.ok(Map.of("success", "true", "token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return sessionService.logout(refreshToken);
    }

    @PostMapping("/magic/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody MagicLinkRequest req) {
        return sessionService.sendMagicLink(req.getEmail());
    }

    @GetMapping("/magic/verify")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token, HttpServletRequest req) {
        return sessionService.verifyMagicToken(token, req);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return sessionService.refresh(refreshToken);
    }
}