package com.arsuhinars.animals_chipization.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class UniqueItemsValidator implements ConstraintValidator<UniqueItems, List<?>> {
    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext constraintValidatorContext) {
        return Set.copyOf(value).size() == value.size();
    }
}
