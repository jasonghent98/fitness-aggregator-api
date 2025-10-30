package com.jasonghent98.fitness_aggregator_api.controller.auth;

import com.jasonghent98.fitness_aggregator_api.config.provider.oura.OuraConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/oura/auth")
public class OuraAuthController {

    private final OuraConfig oura;

    public OuraAuthController(OuraConfig oura) {
        this.oura = oura;
    }

    /** 302 → Oura OAuth consent screen */
    @GetMapping("/login")
    public ResponseEntity<Void> redirectToOura() {
        // Optional: CSRF/anti-replay; persist this if you plan to validate it in /callback
        String state = UUID.randomUUID().toString();

        String url = oura.getAuthorizeUrl()
                + "?client_id="     + enc(oura.getClientId())
                + "&response_type=" + "code"
                + "&redirect_uri="  + enc(oura.getRedirectUrl())
                + "&scope="         + enc(oura.getScopes())
                + "&state="         + enc(state);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}