package io.github.sivaramanr.genericmodel.converter;

import io.github.sivaramanr.common.types.StatusType;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class StatusTypeConverter extends BaseEnumConverter<StatusType> {

    public StatusTypeConverter() {
        super(StatusType.class);
    }

}
