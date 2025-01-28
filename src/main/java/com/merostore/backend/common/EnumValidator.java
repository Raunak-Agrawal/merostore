package com.merostore.backend.common;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class EnumValidator implements ConstraintValidator<ValidateEnum, EnumBase> {
    private final Set<String> allowedValues = new HashSet<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void initialize(ValidateEnum constraintAnnotation) {
        Class<? extends EnumBase> enumClass = constraintAnnotation.enumClass();
        EnumBase[] enumConstants = enumClass.getEnumConstants();

        for (EnumBase iEnum : enumConstants) {
            allowedValues.add(iEnum.getCode());
        }
    }

    @Override
    public boolean isValid(EnumBase param, ConstraintValidatorContext context) {
        if (param == null) {
            return false;
        }
        return allowedValues.contains(param.getCode());
    }
} 