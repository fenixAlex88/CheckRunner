package ru.clevertec.check.validator;

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
    @DisplayName("Product ID Quantity Validator")
    void testProductIdQuantityValidator() {
        assertAll(
                () -> assertTrue(productArgumentValidator.validate("1-6")),
                () -> assertFalse(productArgumentValidator.validate("1-abc")),
                () -> assertFalse(productArgumentValidator.validate(null)),
                () -> assertFalse(productArgumentValidator.validate("")),
                () -> assertFalse(productArgumentValidator.validate("abc-def"))
        );
    }

    @Test
    @DisplayName("Discount Card Validator")
    void testDiscountCardValidator() {
        assertAll(
                () -> assertTrue(discountCardValidator.validate("discountCard=1234")),
                () -> assertFalse(discountCardValidator.validate("discountCard=abcd")),
                () -> assertFalse(discountCardValidator.validate(null)),
                () -> assertFalse(discountCardValidator.validate("")),
                () -> assertFalse(discountCardValidator.validate("abc-def"))
        );
    }

    @Test
    @DisplayName("Balance Debit Card Validator")
    void testBalanceDebitCardValidator() {
        assertAll(
                () -> assertTrue(balanceDebitCardValidator.validate("balanceDebitCard=123.45")),
                () -> assertFalse(balanceDebitCardValidator.validate("balanceDebitCard=abc")),
                () -> assertFalse(balanceDebitCardValidator.validate(null)),
                () -> assertFalse(balanceDebitCardValidator.validate("")),
                () -> assertFalse(balanceDebitCardValidator.validate("123"))
        );
    }

    @Test
    @DisplayName("Save to File Validator")
    void testSaveToFileValidator() {
        assertAll(
                () -> assertTrue(saveToFileValidator.validate("saveToFile=result.csv")),
                () -> assertFalse(saveToFileValidator.validate("saveToFile=result.txt")),
                () -> assertFalse(saveToFileValidator.validate(null)),
                () -> assertFalse(saveToFileValidator.validate("")),
                () -> assertFalse(saveToFileValidator.validate("saveToFile=result"))
        );
    }

    @Test
    @DisplayName("Datasource URL Validator")
    void testDatasourceUrlValidator() {
        assertAll(
                () -> assertTrue(datasourceUrlValidator.validate("datasource.url=jdbc:postgresql://localhost:5432/mydb")),
                () -> assertFalse(datasourceUrlValidator.validate("datasource.url=invalid_url")),
                () -> assertFalse(datasourceUrlValidator.validate(null)),
                () -> assertFalse(datasourceUrlValidator.validate("")),
                () -> assertFalse(datasourceUrlValidator.validate("datasource.url=jdbc:mysql://localhost:5432/mydb"))
        );
    }

    @Test
    @DisplayName("Datasource User Validator")
    void testDatasourceUserValidator() {
        assertAll(
                () -> assertTrue(datasourceUserValidator.validate("datasource.username=user")),
                () -> assertFalse(datasourceUserValidator.validate("datasource.username=")),
                () -> assertFalse(datasourceUserValidator.validate(null)),
                () -> assertFalse(datasourceUserValidator.validate("")),
                () -> assertFalse(datasourceUserValidator.validate("datasource.username"))
        );
    }

    @Test
    @DisplayName("Datasource Password Validator")
    void testDatasourcePasswordValidator() {
        assertAll(
                () -> assertTrue(datasourcePasswordValidator.validate("datasource.password=secret")),
                () -> assertFalse(datasourcePasswordValidator.validate("datasource.password=")),
                () -> assertFalse(datasourcePasswordValidator.validate(null)),
                () -> assertFalse(datasourcePasswordValidator.validate("")),
                () -> assertFalse(datasourcePasswordValidator.validate("datasource.password"))
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
