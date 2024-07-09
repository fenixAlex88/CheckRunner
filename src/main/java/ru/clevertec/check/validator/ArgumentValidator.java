package ru.clevertec.check.validator;

public interface ArgumentValidator {
    String ID_QUANTITY_REGEX = "\\d+-\\d+";
    String DISCOUNT_CARD_REGEX = "discountCard=\\d{4}$";
    String BALANCE_DEBIT_CARD_REGEX = "balanceDebitCard=-\\d+(\\.\\d{1,2})?$";

    boolean validate(String argument);
}
