package io.github.sivaramanr.genericmodel.converter;

import io.github.sivaramanr.common.types.GenericType;
import jakarta.persistence.Converter;

@Converter
public class GenericTypeConverter extends BaseEnumConverter<GenericType> {

    public GenericTypeConverter() {
        super(GenericType.class);
    }

}
