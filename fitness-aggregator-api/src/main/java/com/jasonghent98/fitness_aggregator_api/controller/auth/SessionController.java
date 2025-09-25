package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.auth.MagicLinkRequest;
import com.jasonghent98.fitness_aggregator_api.service.auth.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        UUID userId = UserContext.getUserId();
        return ResponseEntity.ok(sessionService.whoAmI(userId));
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
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {
        return sessionService.verifyMagicToken(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return sessionService.refresh(refreshToken);
    }
}