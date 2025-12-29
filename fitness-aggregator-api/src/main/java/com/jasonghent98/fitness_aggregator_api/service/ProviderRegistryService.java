package com.jasonghent98.fitness_aggregator_api.service;

import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderRepository;
import lombok.Data;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Component
public class ProviderRegistryService {

    private final ProviderRepository providerRepo;

    // atomically replaceable maps for thread-safety
    private final AtomicReference<Map<String, Provider>> codeToProvidersCache = new AtomicReference<>(Map.of());

    public ProviderRegistryService(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    /** Loads all providers into thread-safe memory on app startup*/
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void loadOnStartup() {
        refresh();
    }

    @Transactional(readOnly = true)
    public void refresh() {
        List<Provider> allProviders = providerRepo.findAll();
        Map<String, Provider> codeToProvider = new HashMap<>();
        for (Provider p : allProviders) {
            // prefer a stable "code" slug if you have it; fallback to lowercased name
            if (p != null && p.getName() != null && !p.getName().isBlank()) {
                codeToProvider.put(p.getName(), p);
            }
        }
        codeToProvidersCache.set(Collections.unmodifiableMap(codeToProvider));
    }
}
