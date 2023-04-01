package com.arsuhinars.animals_chipization.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FloatingMinValidator.class)
@Target({
    ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatingMin {
    String message() default "Value must be greater than {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    float value();

    boolean inclusive() default false;
}
