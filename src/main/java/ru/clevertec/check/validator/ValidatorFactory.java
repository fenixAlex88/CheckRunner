package ru.clevertec.check.validator;

public class ValidatorFactory {
    public static ArgumentValidator createValidator(String regex) {
        return argument -> argument != null && argument.matches(regex);
    }
}
