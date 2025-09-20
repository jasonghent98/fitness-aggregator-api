package com.jasonghent98.fitness_aggregator_api.config.persistance.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class StringIntegerMapConverter extends AbstractJsonAttributeConverter<Map<String, Integer>> {
    public StringIntegerMapConverter() {
        super(new TypeReference<>() {});
    }
}