package io.github.sivaramanr.genericmodel.converter;

import io.github.sivaramanr.common.types.ValueType;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ValueTypeConverter extends BaseEnumConverter<ValueType> {

    public ValueTypeConverter() {
        super(ValueType.class);
    }

}
