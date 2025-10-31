// FitbitActivityEntry.java
package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name="fitbit_activity_summaries",
        uniqueConstraints=@UniqueConstraint(name="uq_fitbit_activities", columnNames={"actualize_user_id","log_id"}),
        indexes=@Index(name="idx_fitbit_activities_user_date", columnList="actualize_user_id,activity_date"))
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FitbitActivitySummary {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) private UUID id; // internal primary key;
    @Column(name="actualize_user_id", nullable=false) private UUID actualizeUserId;
    @Column(name="fitbit_user_id", length=64, nullable=false) private String fitbitUserId;
    @Column(name="log_id", nullable=false) private Long logId;

    @Column(name="activity_date", nullable=false) private LocalDate activityDate;
    @Column(name="start_time_local") private LocalTime startTimeLocal;
    @Column(name="duration_ms") private Long durationMs;
    private Integer calories;
    private Double distance;
    private Integer steps;
    @Column(name="activity_name") private String activityName;
    @Column(name="tcx_link") private String tcxLink;

    @Column(name="created_at", nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt = OffsetDateTime.now();
}