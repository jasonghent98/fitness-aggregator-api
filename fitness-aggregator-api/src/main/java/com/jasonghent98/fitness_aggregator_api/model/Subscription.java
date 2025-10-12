package com.jasonghent98.fitness_aggregator_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Data
@Builder
@Entity
@Table(
        name = "subscriptions",
        indexes = {
                @Index(name = "idx_subscriptions_user", columnList = "user_id"),
                @Index(name = "idx_subscriptions_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_subscriptions_stripe_subscription_id", columnNames = "stripe_subscription_id")
        }
)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /** FK to users.id */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /** Stripe identifiers */
    @Column(name = "stripe_subscription_id", nullable = false, unique = true)
    private String stripeSubscriptionId;     // sub_***

    @Column(name = "stripe_customer_id", nullable = false)
    private String stripeCustomerId;         // cus_***

    @Column(name = "stripe_schedule_id")
    private String stripeScheduleId;         // optional (subscription schedules)

    /** Product & pricing snapshot */
    @Column(name = "product_key", nullable = false)
    private String productKey;               // e.g. MEMBERSHIP_ENHANCED, COACHING_12W

    @Column(name = "price_id", nullable = false)
    private String priceId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Lifecycle + period windows */
    @Column(name = "status", nullable = false)
    private String status;                   // 'incomplete','trialing','active','past_due','canceled','unpaid'

    @Column(name = "current_period_start")
    private Instant currentPeriodStart;

    @Column(name = "current_period_end")
    private Instant currentPeriodEnd;

    @Column(name = "cancel_at")
    private Instant cancelAt;

    @Column(name = "canceled_at")
    private Instant canceledAt;

    /** Money snapshot (optional but handy for audit/UI) */
    @Column(name = "amount_cents")
    private Long amountCents;

    @Column(name = "currency")
    private String currency;                 // 'usd'

    @Column(name = "interval")
    private String interval;                 // 'week','month','year'

    /** Free-form metadata (stored as JSONB) */
    @Column(name = "meta", columnDefinition = "jsonb")
    private String meta;

    /** Timestamps */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Convenience: treat trialing/active as “active” */
    @Transient
    public boolean isActiveLike() {
        return "active".equalsIgnoreCase(status) || "trialing".equalsIgnoreCase(status);
    }

    /** Defaults */
    @PrePersist
    public void prePersist() {
        if (quantity == null) quantity = 1;
    }
}