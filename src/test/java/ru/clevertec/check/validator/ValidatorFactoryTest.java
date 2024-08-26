package ru.clevertec.check.validator;

import jdk.jfr.Enabled;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidatorFactory Tests")
class ValidatorFactoryTest {

    private final ArgumentValidator productArgumentValidator = new ProductArgumentValidator();
    private final ArgumentValidator discountCardValidator = ValidatorFactory.createValidator(ArgumentValidator.DISCOUNT_CARD_REGEX);
    private final ArgumentValidator balanceDebitCardValidator = ValidatorFactory.createValidator(ArgumentValidator.BALANCE_DEBIT_CARD_REGEX);
    private final ArgumentValidator saveToFileValidator = ValidatorFactory.createValidator(ArgumentValidator.SAVE_TO_FILE_REGEX);
    private final ArgumentValidator datasourceUrlValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_URL_REGEX);
    private final ArgumentValidator datasourceUserValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_USER_REGEX);
    private final ArgumentValidator datasourcePasswordValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_PASSWORD_REGEX);

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
