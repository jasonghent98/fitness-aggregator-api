package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.dto.WaitlistRequest;
import com.jasonghent98.fitness_aggregator_api.model.Lead;
import com.jasonghent98.fitness_aggregator_api.repository.LeadRepository;
import com.jasonghent98.fitness_aggregator_api.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    private final LeadRepository leads;
    private static final Pattern COMMA_SPLIT = Pattern.compile("\\s*,\\s*");

    public WaitlistController(LeadRepository leads) {
        this.leads = leads;
    }

    @PostMapping
    public ResponseEntity<?> joinWaitlist(@RequestBody WaitlistRequest body,
                                          HttpServletRequest req) {
        // Manual validation (no jakarta.validation)
        if (!ValidationUtil.isValidEmail(body.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid email"));
        }
        // Capture UA + IP (no signing)
        String userAgent = Optional.ofNullable(req.getHeader("User-Agent")).orElse(null);
        String clientIp = extractClientIp(req);

        // Upsert on email (avoid dup leads)
        Lead lead = leads.findByEmailIgnoreCase(body.getEmail())
                .orElseGet(Lead::new);

        lead.setEmail(body.getEmail().trim().toLowerCase());
        lead.setSource(body.getSource());
        lead.setIp(clientIp);
        lead.setSurveyAnswers(body.getSurvey());
        lead.setUserAgent(userAgent);

        leads.save(lead);

        return ResponseEntity.ok(Map.of("ok", true));
    }

    // return the canonical ip from client
    private String extractClientIp(HttpServletRequest req) {
        // 1) X-Forwarded-For: "client, proxy1, proxy2"
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            String[] parts = COMMA_SPLIT.split(xff);
            if (parts.length > 0) {
                return stripPort(parts[0]);
            }
        }
        // 2) X-Real-IP (some proxies)
        String xri = req.getHeader("X-Real-IP");
        if (xri != null && !xri.isBlank()) {
            return stripPort(xri);
        }
        // 3) Fallback to the connection peer
        return stripPort(req.getRemoteAddr());
    }

    // Reliably capture the original client IP when requests come through
    private String stripPort(String ip) {
        if (ip == null) return null;
        // Remove IPv6 zone/port notations if present
        if (ip.startsWith("[")) {
            int end = ip.indexOf(']');
            return (end > 0) ? ip.substring(1, end) : ip;
        }
        int colon = ip.indexOf(':');
        if (colon > -1 && ip.indexOf('.') == -1) { // likely IPv6; keep full
            return ip;
        }
        // IPv4: drop :port if any
        return colon > -1 ? ip.substring(0, colon) : ip;
    }
}
