package com.merostore.backend.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = EnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //METHOD, FIELD, etc.
public @interface ValidateEnum {
    String message() default "is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return Specifies the enumeration type. The parameter value must be a value in this enumeration type
     */
    Class<? extends EnumBase> enumClass();

}