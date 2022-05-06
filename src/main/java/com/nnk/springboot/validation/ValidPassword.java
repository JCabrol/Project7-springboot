package com.nnk.springboot.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Your password is not valid, to be valid it should contain at least: 8 characters, one uppercase letter and one lowercase letter, one digit and one symbol.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

