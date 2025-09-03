package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.dto.WaitlistRequest;
import com.jasonghent98.fitness_aggregator_api.model.Lead;
import com.jasonghent98.fitness_aggregator_api.repository.LeadRepository;
import com.jasonghent98.fitness_aggregator_api.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    private final LeadRepository leads;

    public WaitlistController(LeadRepository leads) {
        this.leads = leads;
    }

    @PostMapping
    public ResponseEntity<?> joinWaitlist(@RequestBody WaitlistRequest body,
                                          HttpServletRequest http) {
        // Manual validation (no jakarta.validation)
        if (!ValidationUtil.isValidEmail(body.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid email"));
        }

        // Upsert on email (avoid dup leads)
        Lead lead = leads.findByEmailIgnoreCase(body.getEmail())
                .orElseGet(Lead::new);

        lead.setEmail(body.getEmail().trim().toLowerCase());
        lead.setSource(body.getSource());
        lead.setIp(getClientIp(http));
        lead.setUserAgent(http.getHeader("User-Agent"));

        leads.save(lead);

        return ResponseEntity.ok(Map.of("ok", true));
    }

    // test this via requests before running campaign
    private static String getClientIp(HttpServletRequest req) {
        String h = req.getHeader("x-forwarded-for");
        if (ValidationUtil.notBlank(h)) {
            // first IP in list
            int comma = h.indexOf(',');
            return comma > 0 ? h.substring(0, comma).trim() : h.trim();
        }
        return req.getRemoteAddr();
    }
}
