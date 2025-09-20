package com.jasonghent98.fitness_aggregator_api.service.garmin;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class GarminService {
    public List<Map<String, Object>> getActivities(int page, int size, Instant since) {
        // TODO: return paginated DB results
        return List.of();
    }

    public void triggerSync(String mode) {
        // TODO: enqueue a sync job (recent vs full backfill)
    }

    public void disconnect() {
        // TODO: soft deactivate ProviderAccount
    }
}