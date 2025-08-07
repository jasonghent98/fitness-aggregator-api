package com.jasonghent98.fitness_aggregator_api.model.strava;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(
        name = "strava_activities",
        uniqueConstraints = @UniqueConstraint(columnNames = "strava_activity_id"),
        indexes = {
            @Index(name = "idx_strava_activities_user_id", columnList = "user_id")
        }
)
@Data
public class StravaActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Internal PK

    @Column(name = "strava_activity_id", nullable = false, unique = true)
    private Long stravaActivityId; // From Strava API "id"

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Internal user reference

    // Identifiers
    @Column(name = "external_id")
    private String externalId;

    @Column(name = "upload_id")
    private Long uploadId;

    // Descriptive
    private String name;
    private String type;
    private String sportType;
    private String gearId;
    private String locationCountry;

    // Core metrics
    private Float distance;
    private Integer movingTime;
    private Integer elapsedTime;
    private Float totalElevationGain;
    private Float averageSpeed;
    private Float maxSpeed;
    private Float averageCadence;
    private Float averageWatts;
    private Integer weightedAverageWatts;
    private Integer maxWatts;
    private Float kilojoules;
    private Float averageHeartrate;
    private Integer maxHeartrate;
    private Integer sufferScore;

    // Engagement
    private Integer kudosCount;
    private Integer commentCount;
    private Integer prCount;

    // Timestamps
    private Instant startDate;
    private Instant startDateLocal;

    @Column(name = "fetched_at", nullable = false)
    private Instant fetchedAt = Instant.now(); // When we fetched from Strava

    /*
    public UUID getId() { return id; }
    public Long getStravaActivityId() { return stravaActivityId; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public Float getDistance() { return distance; }
    public Integer getMovingTime() { return movingTime; }
    public Integer getElapsedTime() { return elapsedTime; }
    public Float getTotalElevationGain() { return totalElevationGain; }
    public String getType() { return type; }
    public String getSportType() { return sportType; }
    public Long getUploadId() { return uploadId; }
    public String getExternalId() { return externalId; }
    public Instant getStartDate() { return startDate; }
    public Instant getStartDateLocal() { return startDateLocal; }
    public String getLocationCountry() { return locationCountry; }
    public Integer getKudosCount() { return kudosCount; }
    public Integer getCommentCount() { return commentCount; }
    public String getGearId() { return gearId; }
    public Float getAverageSpeed() { return averageSpeed; }
    public Float getMaxSpeed() { return maxSpeed; }
    public Float getAverageCadence() { return averageCadence; }
    public Float getAverageWatts() { return averageWatts; }
    public Integer getWeightedAverageWatts() { return weightedAverageWatts; }
    public Float getKilojoules() { return kilojoules; }
    public Float getAverageHeartrate() { return averageHeartrate; }
    public Integer getMaxHeartrate() { return maxHeartrate; }
    public Integer getMaxWatts() { return maxWatts; }
    public Integer getPrCount() { return prCount; }
    public Integer getSufferScore() { return sufferScore; }
     */
}
