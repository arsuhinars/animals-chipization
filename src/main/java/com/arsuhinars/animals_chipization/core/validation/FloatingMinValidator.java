package com.arsuhinars.animals_chipization.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FloatingMinValidator implements ConstraintValidator<FloatingMin, Float> {
    private float minValue;
    private boolean inclusive;

    @Override
    public void initialize(FloatingMin constrainAnnotation) {
        minValue = constrainAnnotation.value();
        inclusive = constrainAnnotation.inclusive();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || (inclusive ? value >= minValue : value > minValue);
    }
}
