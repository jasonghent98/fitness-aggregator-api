package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.dto.UserPreferencesRequest;
import com.jasonghent98.fitness_aggregator_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@RequestBody UserPreferencesRequest body) {
        UUID userId = UserContext.getUserId();
        userService.updatePreferences(userId, body.getPersonalization(), body.getStyle(), body.getPersonalization());
        return ResponseEntity.ok().build();
    }
}
