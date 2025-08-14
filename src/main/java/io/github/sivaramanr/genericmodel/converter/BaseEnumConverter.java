package io.github.sivaramanr.genericmodel.converter;

import jakarta.persistence.AttributeConverter;

public abstract class BaseEnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected BaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return Enum.valueOf(enumClass, dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
