package com.nikozka.springapp.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IINValidator.class)
public @interface ValidIIN {
    String message() default "Invalid IIN format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
