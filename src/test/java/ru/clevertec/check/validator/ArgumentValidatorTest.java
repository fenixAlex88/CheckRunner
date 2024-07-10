package ru.clevertec.check.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentValidatorTest {

    @Test
    public void testIdQuantityValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.ID_QUANTITY_REGEX);
        assertTrue(validator.validate("123-456"));
        assertFalse(validator.validate("123-abc"));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testDiscountCardValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.DISCOUNT_CARD_REGEX);
        assertTrue(validator.validate("discountCard=1234"));
        assertFalse(validator.validate("discountCard=123"));
        assertFalse(validator.validate("discountCard=abcd"));
        assertFalse(validator.validate("discountCard=12345"));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testBalanceDebitCardValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.BALANCE_DEBIT_CARD_REGEX);
        assertTrue(validator.validate("balanceDebitCard=123"));
        assertTrue(validator.validate("balanceDebitCard=-123"));
        assertTrue(validator.validate("balanceDebitCard=123.45"));
        assertFalse(validator.validate("balanceDebitCard=123.456"));
        assertFalse(validator.validate("balanceDebitCard=abc"));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testSaveToFileValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.SAVE_TO_FILE_REGEX);
        assertTrue(validator.validate("saveToFile=data.csv"));
        assertFalse(validator.validate("saveToFile=data.txt"));
        assertFalse(validator.validate("saveToFile=data.csv.exe"));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testDatasourceUrlValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_URL_REGEX);
        assertTrue(validator.validate("datasource.url=jdbc:postgresql://localhost:5432/mydb"));
        assertFalse(validator.validate("datasource.url="));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testDatasourceUserValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_USER_REGEX);
        assertTrue(validator.validate("datasource.username=user"));
        assertFalse(validator.validate("datasource.username="));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testDatasourcePasswordValidator() {
        ArgumentValidator validator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_PASSWORD_REGEX);
        assertTrue(validator.validate("datasource.password=pass"));
        assertFalse(validator.validate("datasource.password="));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }

    @Test
    public void testProductArgumentValidator() {
        ProductArgumentValidator validator = new ProductArgumentValidator();
        assertTrue(validator.validate("123-456"));
        assertFalse(validator.validate("123-abc"));
        assertFalse(validator.validate("123456"));
        assertFalse(validator.validate(""));
        assertFalse(validator.validate(null));
    }
}
