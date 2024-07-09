package ru.clevertec.check.validator;

// A specialized validator for products and their quantity
public class ProductArgumentValidator implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return argument.matches(ID_QUANTITY_REGEX);
    }
}
