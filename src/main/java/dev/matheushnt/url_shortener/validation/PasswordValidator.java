package dev.matheushnt.url_shortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern NUMBER = Pattern.compile("[0-9]");
    private static final Pattern SYMBOL = Pattern.compile("[!@#$%&*_]");

    private static final int MIN_LENGTH = 8;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (!UPPER.matcher(value).find()) {
            context.buildConstraintViolationWithTemplate("deve conter ao menos uma letra maiúscula")
                    .addConstraintViolation();
            valid = false;
        }
        if (!LOWER.matcher(value).find()) {
            context.buildConstraintViolationWithTemplate("deve conter ao menos uma letra minúscula")
                    .addConstraintViolation();
            valid = false;
        }
        if (!NUMBER.matcher(value).find()) {
            context.buildConstraintViolationWithTemplate("deve conter ao menos um número")
                    .addConstraintViolation();
            valid = false;
        }
        if (!SYMBOL.matcher(value).find()) {
            context.buildConstraintViolationWithTemplate("deve conter ao menos um símbolo (!@#$%&*_)")
                    .addConstraintViolation();
            valid = false;
        }
        if (value.length() < MIN_LENGTH) {
            context.buildConstraintViolationWithTemplate("deve ter no mínimo " + MIN_LENGTH + " caracteres")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
