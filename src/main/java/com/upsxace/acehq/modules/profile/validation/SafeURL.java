package com.upsxace.acehq.modules.profile.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SafeURLValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeURL {
    String message() default "Unsafe URL.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
