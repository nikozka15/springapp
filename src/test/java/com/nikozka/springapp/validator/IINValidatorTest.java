package com.nikozka.springapp.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IINValidatorTest {

    private final IINValidator validator = new IINValidator();

    @Test
    void isValidTestValidIIN() {
        String validIIN = "3483310183";
        assertTrue(validator.isValid(validIIN, null));
    }

    @Test
    void isValidTestInvalidLength() {
        assertAll("Invalid length",
                () -> assertFalse(validator.isValid("12345", null), "IIN with 5 digits is invalid"),
                () -> assertFalse(validator.isValid("12345678900", null), "IIN with 11 digits is invalid")
        );
    }

    @Test
    void testValidFormatWithCharacter() {
        String validIINWithCharacter = "123a567890";
        assertFalse(validator.isValid(validIINWithCharacter, null));
    }

    @Test
    void isValidTestInvalidControlNumber() {
        assertAll("All possible control numbers",
                () -> assertFalse(validator.isValid("3483310181", null), "Control number 1 is invalid"),
                () -> assertFalse(validator.isValid("3483310182", null), "Control number 2 is invalid"),
                () -> assertTrue(validator.isValid("3483310183", null), "Control number 3 is valid"),
                () -> assertFalse(validator.isValid("3483310184", null), "Control number 4 is invalid"),
                () -> assertFalse(validator.isValid("3483310185", null), "Control number 5 is invalid"),
                () -> assertFalse(validator.isValid("3483310186", null), "Control number 6 is invalid"),
                () -> assertFalse(validator.isValid("3483310187", null), "Control number 7 is invalid"),
                () -> assertFalse(validator.isValid("3483310188", null), "Control number 8 is invalid"),
                () -> assertFalse(validator.isValid("3483310189", null), "Control number 9 is invalid"),
                () -> assertFalse(validator.isValid("3483310180", null), "Control number 0 is invalid")
        );
    }
}
