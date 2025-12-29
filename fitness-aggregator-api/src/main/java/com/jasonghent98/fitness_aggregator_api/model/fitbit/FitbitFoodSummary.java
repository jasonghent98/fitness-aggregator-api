// FitbitFoodEntry.java
package com.jasonghent98.fitness_aggregator_api.model.fitbit;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name="fitbit_food_summaries",
        uniqueConstraints=@UniqueConstraint(name="uq_fitbit_food", columnNames={"actualize_user_id","log_id"}),
        indexes=@Index(name="idx_fitbit_food_user_date", columnList="actualize_user_id,log_date"))
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FitbitFoodSummary {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) private UUID id; // internal primary key;
    @Column(name="actualize_user_id", nullable=false) private UUID actualizeUserId;
    @Column(name="fitbit_user_id", length=64, nullable=false) private String fitbitUserId;
    @Column(name="log_id", nullable=false) private Long logId;

    @Column(name="log_date", nullable=false) private LocalDate logDate;
    @Column(name="meal_type_id") private Integer mealTypeId;
    private Double amount;
    @Column(name="unit_name") private String unitName;
    @Column(name="unit_type") private String unitType;

    @Column(name="food_name") private String foodName;
    private String brand;
    @Column(name="serving_size") private Double servingSize;

    private Double calories;
    private Double carbs;
    private Double fat;
    private Double fiber;
    private Double protein;
    private Double sodium;

    @Column(name="created_at", nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt = OffsetDateTime.now();
}