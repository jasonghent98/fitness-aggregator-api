package com.jasonghent98.fitness_aggregator_api.config.persistance.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.Optional;

public abstract class AbstractJsonAttributeConverter<T> implements AttributeConverter<T, String> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<T> typeReference;

    protected AbstractJsonAttributeConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return Optional.ofNullable(attribute).map(a -> {
            try {
                return mapper.writeValueAsString(a);
            } catch (Exception e) {
                throw new RuntimeException("Could not convert entity attribute to JSON", e);
            }
        }).orElse(null);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData).map(json -> {
            try {
                return mapper.readValue(json, typeReference);
            } catch (Exception e) {
                throw new RuntimeException("Could not convert JSON to entity attribute", e);
            }
        }).orElse(null);
    }
}