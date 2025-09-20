package com.jasonghent98.fitness_aggregator_api.config.persistance.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class StringObjectMapConverter extends AbstractJsonAttributeConverter<Map<String, Object>> {
    public StringObjectMapConverter() {
        super(new TypeReference<>() {});
    }
}