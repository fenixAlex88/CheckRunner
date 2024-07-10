package ru.clevertec.check.validator;

public interface ArgumentValidator {
    String ID_QUANTITY_REGEX = "\\d+-\\d+";
    String DISCOUNT_CARD_REGEX = "discountCard=\\d{4}$";
    String BALANCE_DEBIT_CARD_REGEX = "balanceDebitCard=-?\\d+(\\.\\d{1,2})?";
    String PATH_TO_FILE_REGEX = "pathToFile=.+\\.csv$";
    String SAVE_TO_FILE_REGEX = "saveToFile=.+\\.csv$";


    boolean validate(String argument);
}
