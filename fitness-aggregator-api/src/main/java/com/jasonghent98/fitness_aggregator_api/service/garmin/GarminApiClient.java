package com.jasonghent98.fitness_aggregator_api.service.garmin;

import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.*;
import com.jasonghent98.fitness_aggregator_api.model.ProviderAccount;
import com.jasonghent98.fitness_aggregator_api.service.ProviderAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GarminApiClient {

    private final RestTemplate restTemplate;
    private final ProviderAccountService providerAccountService;

    @Value("${garmin.BACKFILL_URL:https://apis.garmin.com/wellness-api}")
    private String garminBackfillUrl;

    public List<GarminDailySummaryPayload.DailySummary> fetchDaily(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/rest/dailies";
        GarminDailySummaryPayload payload = get(
                url,
                GarminDailySummaryPayload.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.getDailySummaries() != null
                ? payload.getDailySummaries()
                : Collections.emptyList();
    }

    public List<GarminSleepSummaryPayload.SleepSummary> fetchSleep(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/users/{uid}/sleep?start={start}&end={end}";
        GarminSleepSummaryPayload payload = get(
                url,
                GarminSleepSummaryPayload.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.getSleepSummaries() != null
                ? payload.getSleepSummaries()
                : Collections.emptyList();
    }

    public List<GarminStressSummaryPayload.StressSummary> fetchStress(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/users/{uid}/stress?start={start}&end={end}";
        GarminStressSummaryPayload payload = get(
                url,
                GarminStressSummaryPayload.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.getStressSummaries() != null
                ? payload.getStressSummaries()
                : Collections.emptyList();
    }

    public List<GarminHrvSummaryPayload.HrvSummary> fetchHrv(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/users/{uid}/hrv?start={start}&end={end}";
        GarminHrvSummaryPayload payload = get(
                url,
                GarminHrvSummaryPayload.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.getHrvSummaries() != null
                ? payload.getHrvSummaries()
                : Collections.emptyList();
    }

    public List<GarminPulseOxSummaryPayload.PulseOxSummary> fetchPulseOx(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/users/{uid}/pulse-ox?start={start}&end={end}";
        GarminPulseOxSummaryPayload payload = get(
                url,
                GarminPulseOxSummaryPayload.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.getPulseOxSummaries() != null
                ? payload.getPulseOxSummaries()
                : Collections.emptyList();
    }

    public List<GarminActivitySummaryPayload.ActivitySummary> fetchActivities(
            UUID userId, LocalDate start, LocalDate end
    ) {
        String token = getBearer(userId);
        String url = garminBackfillUrl + "/users/{uid}/activities?start={start}&end={end}";

        // If Garmin paginates activities, swap to a loop over pages here.
        GarminActivitySummaryPayloadWrapper payload = get(
                url,
                GarminActivitySummaryPayloadWrapper.class,
                Map.of(
                        "uid", userId,
                        "start", start.toString(),
                        "end", end.toString()
                ),
                token
        );
        return payload != null && payload.activityDetails() != null
                ? payload.activityDetails()
                : Collections.emptyList();
    }

    // -------- Internal helpers --------

    private String getBearer(UUID userId) {
        // Reuse your existing lookup: by provider name + providerUserId
        ProviderAccount a = providerAccountService.getProviderAccountForUserAndProvider("garmin", userId);
        // TODO: if expired, refresh via your token refresh flow before returning.
        return a.getAccessToken();
    }

    private <T> T get(String url,
                      Class<T> type,
                      Map<String, ?> uriVars,
                      String bearerToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            if (bearerToken != null && !bearerToken.isBlank()) {
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
            }
            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<T> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    req,
                    type,
                    uriVars
            );
            System.out.println(resp + " from get() in GarminApiClient");
            return resp.getStatusCode().is2xxSuccessful() ? resp.getBody() : null;
        } catch (RestClientException ex) {
            log.warn("Garmin GET failed: {} {}", url, ex.getMessage());
            return null;
        }
    }

    // If you need list-of-something without a wrapper:
    private <T> List<T> getList(String url,
                                ParameterizedTypeReference<List<T>> ref,
                                Map<String, ?> uriVars,
                                String bearerToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            if (bearerToken != null && !bearerToken.isBlank()) {
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
            }
            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<List<T>> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    req,
                    ref,
                    uriVars
            );
            return resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null
                    ? resp.getBody()
                    : Collections.emptyList();
        } catch (RestClientException ex) {
            log.warn("Garmin GET(list) failed: {} {}", url, ex.getMessage());
            return Collections.emptyList();
        }
    }

    // Small wrapper for activities to match your DTO list name
    public record GarminActivitySummaryPayloadWrapper(
            List<GarminActivitySummaryPayload.ActivitySummary> activityDetails
    ) {}
}