package org.devsync.spring.common.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword,String> {

    @Override
    public boolean isValid(String password,
                           ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return password.matches(
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$"
        );
    }
}
