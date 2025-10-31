package com.jasonghent98.fitness_aggregator_api.service.fitbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitActivityLog;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitBodyLog;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitFoodLog;
import com.jasonghent98.fitness_aggregator_api.dto.fitbit.FitbitSleepLog;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitActivitySummary;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitBodySummary;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitFoodSummary;
import com.jasonghent98.fitness_aggregator_api.model.fitbit.FitbitSleepSummary;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FitbitMapper {

    // strict HH:mm:ss (24-hour, zero-padded)
    private static final DateTimeFormatter TIME_HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

    FitbitMapper() {}


    // Mapper: DTO -> Entity
    public FitbitActivitySummary mapActivityPayload(
            UUID actualizeUserId,
            String fitbitUserId,
            LocalDate forDate,
            FitbitActivityLog.ActivityLog a
    ) {
        LocalTime localStart = null;
        if (a.getStartTime() != null && !a.getStartTime().isBlank()) {
            localStart = LocalTime.parse(a.getStartTime());
        }

        FitbitActivitySummary e = new FitbitActivitySummary();
        e.setActualizeUserId(actualizeUserId);
        e.setFitbitUserId(fitbitUserId);
        e.setLogId(a.getLogId());                 // UNIQUE with user
        e.setActivityName(a.getActivityName());
        e.setActivityDate(forDate);               // convenience date column
        e.setStartTimeLocal(localStart);              // or store as OffsetDateTime if you have tz
        e.setDurationMs(a.getDurationMs());
        e.setCalories(a.getCalories());
        e.setDistance(a.getDistance());
        e.setSteps(a.getSteps());
        e.setTcxLink(a.getTcxLink());
        return e;
    }

    /* ----------------- SLEEP ----------------- */
    public FitbitSleepSummary mapSleepPayload(
            UUID actualizeUserId,
            String fitbitUserId,
            FitbitSleepLog.SleepSession dto
    ) {
        var m = new FitbitSleepSummary();
        m.setActualizeUserId(actualizeUserId);
        m.setFitbitUserId(fitbitUserId);
        m.setLogId(dto.getLogId());

        if (dto.getDateOfSleep() != null) {
            m.setDateOfSleep(LocalDate.parse(dto.getDateOfSleep()));
        }
        if (dto.getStartTime() != null) {
            m.setStartTime(parseOffset(dto.getStartTime()));
        }
        if (dto.getEndTime() != null) {
            m.setEndTime(parseOffset(dto.getEndTime()));
        }

        m.setDurationMs(dto.getDurationMs());
        m.setEfficiency(dto.getEfficiency());
        m.setIsMainSleep(Boolean.TRUE.equals(dto.getIsMainSleep()));

        // Keep full levels payload as JSON string (jsonb column)
        try {
            m.setLevelsJson(dto.getLevels() != null ? dto.getLevels().toString() : null);
        } catch (Exception e) {
            m.setLevelsJson(null); // fail soft; payload still usable

        }

        m.setCreatedAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        return m;
    }

    /* ----------------- FOOD ----------------- */
    public FitbitFoodSummary mapFoodPayload(
            UUID actualizeUserId,
            String fitbitUserId,
            FitbitFoodLog.FoodEntry dto
    ) {
        var m = new FitbitFoodSummary();
        m.setActualizeUserId(actualizeUserId);
        m.setFitbitUserId(fitbitUserId);
        m.setLogId(dto.getLogId());

        if (dto.getLogDate() != null) {
            m.setLogDate(LocalDate.parse(dto.getLogDate()));
        }
        m.setMealTypeId(dto.getMealTypeId());
        m.setAmount(dto.getAmount());

        if (dto.getUnit() != null) {
            m.setUnitName(dto.getUnit().getName());
            m.setUnitType(dto.getUnit().getType());
        }

        if (dto.getLoggedFood() != null) {
            m.setFoodName(dto.getLoggedFood().getName());
            m.setBrand(dto.getLoggedFood().getBrand());
            m.setServingSize(dto.getLoggedFood().getServingSize());
            // Fitbit sometimes duplicates calories in both places; prefer entry-level nutritionalValues below
            if (dto.getLoggedFood().getCalories() != null && m.getCalories() == null) {
                m.setCalories(safeD(dto.getLoggedFood().getCalories()));
            }
        }

        if (dto.getNutritionalValues() != null) {
            var n = dto.getNutritionalValues();
            m.setCalories(safeD(n.getCalories()));
            m.setCarbs(safeD(n.getCarbs()));
            m.setFat(safeD(n.getFat()));
            m.setFiber(safeD(n.getFiber()));
            m.setProtein(safeD(n.getProtein()));
            m.setSodium(safeD(n.getSodium()));
        }

        m.setCreatedAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        return m;
    }

    /* ----------------- BODY (weight & body-fat share one table) ----------------- */
    public FitbitBodySummary mapBodyPayload(
            UUID actualizeUserId,
            String fitbitUserId,
            FitbitBodyLog.WeightLog dto
    ) {
        var m = new FitbitBodySummary();
        m.setActualizeUserId(actualizeUserId);
        m.setFitbitUserId(fitbitUserId);
        m.setLogId(dto.getLogId());
        if (dto.getDate() != null) m.setDate(LocalDate.parse(dto.getDate()));
        if (dto.getTime() != null) m.setTimeLocal(LocalTime.parse(dto.getTime(), TIME_HH_MM_SS));
        m.setWeightValue(dto.getWeight());
        m.setBmi(dto.getBmi());
        m.setSource(dto.getSource());
        m.setCreatedAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        return m;
    }

    public FitbitBodySummary toBodyFatEntity(
            UUID actualizeUserId,
            String fitbitUserId,
            FitbitBodyLog.BodyFatLog dto
    ) {
        var m = new FitbitBodySummary();
        m.setActualizeUserId(actualizeUserId);
        m.setFitbitUserId(fitbitUserId);
        m.setLogId(dto.getLogId());
        if (dto.getDate() != null) m.setDate(LocalDate.parse(dto.getDate()));
        if (dto.getTime() != null) m.setTimeLocal(LocalTime.parse(dto.getTime(), TIME_HH_MM_SS));
        m.setFatPercent(dto.getFat());
        m.setSource(dto.getSource());
        m.setCreatedAt(OffsetDateTime.now());
        m.setUpdatedAt(OffsetDateTime.now());
        return m;
    }

    /* ----------------- Helpers ----------------- */
    private static OffsetDateTime parseOffset(String iso) {
        try {
            return OffsetDateTime.parse(iso);
        } catch (Exception ignored) {
            // Some payloads may lack offset; assume UTC
            return LocalDateTime.parse(iso.replace("Z","").replace(" ", "T"))
                    .atOffset(ZoneOffset.UTC);
        }
    }

    private static Double safeD(Double d) {
        return (d == null || d.isNaN() || d.isInfinite()) ? null : d;
    }

}