package com.jasonghent98.fitness_aggregator_api.model.strava;
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
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StravaUser stravaUser;

    // Getters and setters
}