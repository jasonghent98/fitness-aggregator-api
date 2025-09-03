package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.dto.WaitlistRequest;
import com.jasonghent98.fitness_aggregator_api.model.Lead;
import com.jasonghent98.fitness_aggregator_api.repository.LeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class WaitlistService {
    private final LeadRepository leadRepo;

    public WaitlistService(LeadRepository leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Transactional
    public Lead upsertLead(WaitlistRequest req, String ip, String userAgent) {
        Optional<Lead> existingOpt = leadRepo.findByEmailIgnoreCase(req.getEmail());

        Lead lead = existingOpt.orElseGet(Lead::new);
        if (lead.getCreatedAt() == null) {
            lead.setCreatedAt(Instant.now());
        }

        lead.setEmail(req.getEmail().trim().toLowerCase());
        lead.setSurveyAnswers(req.getSurvey());               // JSON/JSONB column in Lead (Map<String,Object>)
        lead.setSource(nullToEmpty(req.getSource()));
        lead.setConsentMarketing(Boolean.TRUE.equals(req.getMarketingConsent()));
        lead.setIp(ip);
        lead.setUserAgent(truncate(userAgent, 500));
        lead.setUpdatedAt(Instant.now());

        return leadRepo.save(lead);
    }

    private static String nullToEmpty(String s) { return s == null ? "" : s; }
    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}