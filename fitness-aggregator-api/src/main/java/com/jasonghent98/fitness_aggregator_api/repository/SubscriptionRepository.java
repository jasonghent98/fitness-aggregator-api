package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    /** Find by Stripe subscription id (unique) for idempotent upserts. */
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    /** Quick guard to avoid creating duplicate checkouts when one is already active/trialing/etc. */
    boolean existsByUserIdAndStatusIn(UUID userId, Collection<String> statuses);

    /** Handy reads (optional). */
    List<Subscription> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Subscription> findFirstByUserIdAndStatusInOrderByCreatedAtDesc(UUID userId, Collection<String> statuses);

    /** If you need to map via Stripe customer id. */
    Optional<Subscription> findByStripeCustomerId(String stripeCustomerId);
}