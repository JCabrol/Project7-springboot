package com.nnk.springboot.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.equals("")) {
            return true;
        } else {
            boolean sizeOk = value.length() >= 8;
            boolean notContainsWhiteSpace = value.chars().noneMatch(Character::isWhitespace);
            boolean containsUpperCase = value.chars().anyMatch(Character::isUpperCase);
            boolean containsLowerCase = value.chars().anyMatch(Character::isLowerCase);
            boolean containsNumber = value.chars().anyMatch(Character::isDigit);
            boolean containsSpecialCharacter = value.chars().anyMatch(c -> (!Character.isLetterOrDigit(c)));
            return sizeOk && notContainsWhiteSpace && containsLowerCase && containsUpperCase && containsNumber && containsSpecialCharacter;
        }
    }
}
