package com.jasonghent98.fitness_aggregator_api.model.garmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "garmin_sleep_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarminSleepSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String summaryId;

    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    @Column(name = "user_id", nullable = false)
    private UUID actualizeUserId;

    private LocalDate calendarDate;
    private Long startTimeInSeconds;
    private Integer startTimeOffsetInSeconds;
    private Integer durationInSeconds;

    private Integer deepSleepDurationInSeconds;
    private Integer lightSleepDurationInSeconds;
    private Integer remSleepInSeconds;
    private Integer awakeDurationInSeconds;

    private Integer unmeasurableSleepInSeconds;

    private String validation;

    // You can decide later if you want to persist maps (Spo2, scores) into JSON (Postgres JSONB)
    // @Convert(converter = StringIntegerMapConverter.class)
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Integer> timeOffsetSleepSpo2;

    // @Convert(converter = StringObjectMapConverter.class)
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> sleepScores;

    // @Convert(converter = StringObjectMapConverter.class)
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> overallSleepScore;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<NapSummary> naps;

    @Data
    public static class NapSummary {
        @JsonProperty("napDurationInSeconds")
        private Integer napDurationInSeconds;

        @JsonProperty("napStartTimeInSeconds")
        private Long napStartTimeInSeconds;

        @JsonProperty("napValidation")
        private String napValidation;

        @JsonProperty("napOffsetInSeconds")
        private Integer napOffsetInSeconds;
    }

}