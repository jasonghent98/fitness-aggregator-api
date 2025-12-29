// FitbitBodyLog.java
package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name="fitbit_body_summaries",
        uniqueConstraints=@UniqueConstraint(name="uq_fitbit_body", columnNames={"actualize_user_id","log_id"}),
        indexes=@Index(name="idx_fitbit_body_user_date", columnList="actualize_user_id,date"))
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FitbitBodySummary {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) private UUID id; // internal primary key;
    @Column(name="actualize_user_id", nullable=false) private UUID actualizeUserId;
    @Column(name="fitbit_user_id", length=64, nullable=false) private String fitbitUserId;
    @Column(name="log_id", nullable=false) private Long logId;

    @Column(name="entry_type", nullable=false) private String entryType; // "WEIGHT" | "FAT"
    @Column(name="date", nullable=false) private LocalDate date;
    @Column(name="time_local") private LocalTime timeLocal;

    // weight
    @Column(name="weight_value") private Double weightValue;
    @Column(name="weight_unit")  private String weightUnit; // "kg" | "lb"
    @Column(name="bmi") private Double bmi;

    // fat
    @Column(name="fat_percent") private Double fatPercent;

    @Column(name="source", length=32) private String source;

    @Column(name="created_at", nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt = OffsetDateTime.now();
}