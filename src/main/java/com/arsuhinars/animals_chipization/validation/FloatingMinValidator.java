package com.arsuhinars.animals_chipization.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FloatingMinValidator implements ConstraintValidator<FloatingMin, Float> {
    private float minValue;

    @Override
    public void initialize(FloatingMin constrainAnnotation) {
        minValue = constrainAnnotation.value();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value > minValue;
    }
}
