package com.jasonghent98.fitness_aggregator_api.config.persistance.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jasonghent98.fitness_aggregator_api.dto.garmin.webhook.GarminStressSummaryPayload;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class BodyBatteryActivityEventListConverter
        extends AbstractJsonAttributeConverter<List<GarminStressSummaryPayload.StressSummary.BodyBatteryActivityEvent>> {

    public BodyBatteryActivityEventListConverter() {
        super(new TypeReference<>() {});
    }
}