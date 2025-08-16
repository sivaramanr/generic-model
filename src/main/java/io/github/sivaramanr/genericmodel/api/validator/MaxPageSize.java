package io.github.sivaramanr.genericmodel.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxPageSizeValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxPageSize {

    String message() default "Page size exceeds the maximum allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
