package com.jasonghent98.fitness_aggregator_api.service.sync;

import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.model.Provider;
import com.jasonghent98.fitness_aggregator_api.repository.ProviderAccountRepository;
import com.jasonghent98.fitness_aggregator_api.service.garmin.GarminBackfillService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BackfillService {

    private final TierWindowService windowService;
    private final ProviderAccountRepository paRepo;
    private final GarminBackfillService garminBackfillService;

    public BackfillService(
            TierWindowService windowService,
            ProviderAccountRepository paRepo,
            GarminBackfillService garminBackfillService
    ) {
        this.windowService = windowService;
        this.paRepo = paRepo;
        this.garminBackfillService = garminBackfillService;
    }

    /** Call this on initial connect OR on tier upgrade (provider = garmin now). */
    @Async
    public void triggerBackfill(UUID userId, Provider provider, String tier) {
        ProviderAccount pa = paRepo.findByUserIdAndProvider(userId, provider).orElseThrow(() -> new IllegalArgumentException("No provider account found"));

        var window = windowService.computeWindow(tier);


        switch (pa.getProvider().getName().toLowerCase()) {
            case "garmin" -> garminBackfillService.backfillAll(userId, window.start(), window.end());
            // case "fitbit" -> fitbitIngestion.backfillAll(...);
            // case "oura"   -> ouraIngestion.backfillAll(...);
            default -> {}
        }
    }
}