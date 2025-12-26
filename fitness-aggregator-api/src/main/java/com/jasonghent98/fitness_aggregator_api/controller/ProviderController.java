package com.jasonghent98.fitness_aggregator_api.controller;

import com.jasonghent98.fitness_aggregator_api.context.UserContext;
import com.jasonghent98.fitness_aggregator_api.context.UserContextResolver;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProviderController {
    private ProviderAccountService providerAccountService;
    ProviderController(ProviderAccountService providerAccountService) {
        this.providerAccountService = providerAccountService;
    }

    @GetMapping("/provider-accounts")
    private List<ProviderAccount> retrieveProviderAccountsForUser() {
        return providerAccountService.getProviderAccountsForUser(UserContext.getUserId());
    }

}
