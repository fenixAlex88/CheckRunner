package ru.clevertec.check.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidatorFactory Tests")
class ValidatorFactoryTest {

    private final ArgumentValidator productArgumentValidator = new ProductArgumentValidator();

   @Test
    @DisplayName(" Test Product Argument Validator Incorrect Requests")
    void testProductArgumentValidator() {
        assertAll(
                () -> assertFalse(productArgumentValidator.validate(null)),
                () -> assertFalse(productArgumentValidator.validate("")),
                () -> assertFalse(productArgumentValidator.validate("abc-def"))
        );
    }

    @Test
    @DisplayName("Create Validator with Null Regex")
    void testCreateValidatorWithNullRegex() {
        assertThrows(IllegalArgumentException.class, () -> ValidatorFactory.createValidator(null));
    }

    @Test
    @DisplayName("Create Validator with Empty Regex")
    void testCreateValidatorWithEmptyRegex() {
        assertThrows(IllegalArgumentException.class, () -> ValidatorFactory.createValidator(""));
    }
}
