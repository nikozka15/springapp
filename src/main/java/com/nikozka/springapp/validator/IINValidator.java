package com.nikozka.springapp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
// awtowired ??
public class IINValidator implements ConstraintValidator<ValidIIN, String> {
    private static final Pattern pattern = Pattern.compile("\\d{10}");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() != 10) {
            return false;
        }
        if (!pattern.matcher(value).matches()) {
            return false;
        }
        int[] digits = value.chars().map(Character::getNumericValue).toArray();

        return calculateControlNumber(digits) == digits[9];
    }

    private static int calculateControlNumber(int[] digits) {
        int sum = digits[0] * (-1) + digits[1] * 5 + digits[2] * 7 + digits[3] * 9 +
                digits[4] * 4 + digits[5] * 6 + digits[6] * 10 + digits[7] * 5 +
                digits[8] * 7;

        return (sum % 11) % 10;
    }
}
