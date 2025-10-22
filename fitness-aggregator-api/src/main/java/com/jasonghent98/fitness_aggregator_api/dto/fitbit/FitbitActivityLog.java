import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FitbitActivityLog {

    @JsonProperty("activities")
    private List<ActivityLog> activities;

    @JsonProperty("summary")
    private ActivitySummary summary;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivityLog {
        @JsonProperty("logId")      private Long logId;
        @JsonProperty("activityName") private String activityName;
        @JsonProperty("startTime")  private String startTime; // "HH:mm", local
        @JsonProperty("duration")   private Long durationMs;
        @JsonProperty("calories")   private Integer calories;
        @JsonProperty("distance")   private Double distance; // km or miles per user unit
        @JsonProperty("steps")      private Integer steps;
        @JsonProperty("tcxLink")    private String tcxLink;  // sometimes present
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActivitySummary {
        private Integer steps;
        private Integer caloriesOut;
        private Double floors;
        private Double elevation;
        private List<DistanceByType> distances;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DistanceByType {
        private String activity;  // "tracker","logged","total","run","walk", etc.
        private Double distance;
    }
}