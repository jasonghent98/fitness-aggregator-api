package com.jasonghent98.fitness_aggregator_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "leads",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"),
        indexes = {
                @Index(name = "idx_leads_status", columnList = "status"),
                @Index(name = "idx_leads_created_at", columnList = "created_at")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Use citext in Postgres for case-insensitive unique email:
    // CREATE EXTENSION IF NOT EXISTS citext;
    @Column(nullable = false, unique = true, columnDefinition = "citext")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    /**
     * e.g. "landing:waitlist-2025q3"
     */
    @Column(nullable = false)
    private String source;

    /**
     * pending | confirmed | unsubscribed | bounced | complained
     * (Keep as TEXT for flexibility now.)
     */
    @Column(nullable = false)
    private String status = "pending";

    /**
     * Arbitrary single-survey answers.
     * Hibernate 6: JSONB via @JdbcTypeCode(SqlTypes.JSON)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "survey_answers", columnDefinition = "jsonb")
    private Map<String, Object> surveyAnswers;

    @Column(name = "consent_marketing", nullable = false)
    private boolean consentMarketing = true;

    /** Store as text; you can move to INET column later if you want.
     * Storing this to fight spam, analyze where leads are coming from, and maintain a compliance trail
     */
    private String ip;

    /**
     * Get metadata on the user device to optimize for ux
     */
    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}