package com.jasonghent98.fitness_aggregator_api.config.persistance.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class StringDoubleMapConverter extends AbstractJsonAttributeConverter<Map<String, Double>> {
    public StringDoubleMapConverter() {
        super(new TypeReference<>() {});
    }
}