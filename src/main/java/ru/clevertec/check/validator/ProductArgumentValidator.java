package ru.clevertec.check.validator;

// Специализированный валидатор для продуктов и их количества
public class ProductArgumentValidator implements ArgumentValidator {

    @Override
    public boolean validate(String argument) {
        return argument.matches(ID_QUANTITY_REGEX);
    }
}
