package ru.clevertec.check.validator;

public interface ArgumentValidator {
    String ID_QUANTITY_REGEX = "\\d+-\\d+";
    String DISCOUNT_CARD_REGEX = "discountCard=\\d{4}$";
    String BALANCE_DEBIT_CARD_REGEX = "balanceDebitCard=-?\\d+(\\.\\d{1,2})?";
    String SAVE_TO_FILE_REGEX = "saveToFile=.+\\.csv$";
    String DATASOURCE_URL_REGEX = "datasource.url=jdbc:postgresql://.+";
    String DATASOURCE_USER_REGEX = "datasource.username=.+";
    String DATASOURCE_PASSWORD_REGEX = "datasource.password=.+";

    boolean validate(String argument);
}
