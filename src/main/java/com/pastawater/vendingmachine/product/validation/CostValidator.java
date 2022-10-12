package com.pastawater.vendingmachine.product.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CostValidator implements ConstraintValidator<Cost, Integer> {

    @Override
    public void initialize(Cost constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer v, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(v) && v >= 0 && v % 5 == 0;
    }
}