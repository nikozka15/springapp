package com.nikozka.springapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IINValidator.class)
public @interface ValidIIN {
    String message() default "Invalid IIN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
