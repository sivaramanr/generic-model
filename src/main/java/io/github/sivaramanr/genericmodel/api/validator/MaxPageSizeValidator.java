package io.github.sivaramanr.genericmodel.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MaxPageSizeValidator implements ConstraintValidator<MaxPageSize, Integer> {

    @Value("${generic-model.api.max-page-size}")
    private int maxPageSize;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || value <= maxPageSize;
    }

}
