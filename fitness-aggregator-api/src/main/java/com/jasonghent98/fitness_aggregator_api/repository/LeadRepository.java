package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID> {

    Optional<Lead> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    /**
     * Native UPSERT by email (citext unique).
     * Pass survey JSON as a string; it will be cast to jsonb on the DB side.
     *
     * Fields updated on conflict:
     *  - full_name, source, survey_answers, consent_marketing, ip, user_agent, updated_at
     */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO leads (email, full_name, source, status, survey_answers, consent_marketing, ip, user_agent, created_at, updated_at)
        VALUES (:email, :fullName, :source, COALESCE(:status, 'pending'), CAST(:surveyJson AS jsonb), :consent, :ip, :userAgent, now(), now())
        ON CONFLICT (email)
        DO UPDATE SET
            full_name = EXCLUDED.full_name,
            source = EXCLUDED.source,
            survey_answers = EXCLUDED.survey_answers,
            consent_marketing = EXCLUDED.consent_marketing,
            ip = EXCLUDED.ip,
            user_agent = EXCLUDED.user_agent,
            updated_at = now()
        """, nativeQuery = true)
    int upsertByEmail(
            @Param("email") String email,
            @Param("fullName") String fullName,
            @Param("source") String source,
            @Param("status") String status,
            @Param("surveyJson") String surveyJson,   // JSON string
            @Param("consent") boolean consent,
            @Param("ip") String ip,
            @Param("userAgent") String userAgent
    );
}