// FitbitSleepSession.java
package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.*;
import java.util.UUID;

@Entity
@Table(name="fitbit_sleep_summaries",
        uniqueConstraints=@UniqueConstraint(name="uq_fitbit_sleep", columnNames={"actualize_user_id","log_id"}),
        indexes=@Index(name="idx_fitbit_sleep_user_date", columnList="actualize_user_id,date_of_sleep"))
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FitbitSleepSummary {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) private UUID id; // internal primary key;
    @Column(name="actualize_user_id", nullable=false) private UUID actualizeUserId;
    @Column(name="fitbit_user_id", length=64, nullable=false) private String fitbitUserId;
    @Column(name="log_id", nullable=false) private Long logId;

    @Column(name="date_of_sleep", nullable=false) private LocalDate dateOfSleep;
    @Column(name="start_time") private OffsetDateTime startTime;
    @Column(name="end_time") private OffsetDateTime endTime;
    @Column(name="duration_ms") private Long durationMs;
    private Integer efficiency;
    @Column(name="is_main_sleep") private Boolean isMainSleep;

    @Column(name="levels_json", columnDefinition="jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode levelsJson;

    @Column(name="created_at", nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt = OffsetDateTime.now();
}