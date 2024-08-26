package ru.clevertec.check.validator;

public class ProductArgumentValidator implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return argument != null && argument.matches(ID_QUANTITY_REGEX);
    }
}
