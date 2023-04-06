package com.arsuhinars.animals_chipization.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FloatingMaxValidator implements ConstraintValidator<FloatingMax, Float> {
    private float maxValue;
    private boolean inclusive;

    @Override
    public void initialize(FloatingMax constrainAnnotation) {
        maxValue = constrainAnnotation.value();
        inclusive = constrainAnnotation.inclusive();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || (inclusive ? value <= maxValue : value < maxValue);
    }
}
