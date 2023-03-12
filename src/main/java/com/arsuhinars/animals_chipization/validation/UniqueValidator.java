package com.arsuhinars.animals_chipization.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class UniqueValidator implements ConstraintValidator<Unique, List<?>> {
    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext constraintValidatorContext) {
        return Set.copyOf(value).size() == value.size();
    }
}
