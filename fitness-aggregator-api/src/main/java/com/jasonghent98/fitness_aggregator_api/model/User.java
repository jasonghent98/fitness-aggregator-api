package com.jasonghent98.fitness_aggregator_api.model;
import com.jasonghent98.fitness_aggregator_api.model.strava.StravaUser;
import jakarta.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String fullName;

    // Add any fields you expect to store for your base user

    // Relationships
    // Only exists in Java code to make it easier to navigate from User → StravaUser. Does NOT create a column
    // The "user" field in the StravaUser class is managing the relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StravaUser stravaUser;

    // Getters and setters
}