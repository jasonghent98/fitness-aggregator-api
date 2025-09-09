package com.jasonghent98.fitness_aggregator_api.repository;

import com.jasonghent98.fitness_aggregator_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

// inherits all the CRUD API operations by default: specify any custom queries if needed
public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByEmailIgnoreCase(String email);
}
