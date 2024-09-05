package ru.clevertec.check.validator;

import java.util.Optional;

public class ValidatorFactory {

    private ValidatorFactory() {}

    public static ArgumentValidator createValidator(String regex) {
        return Optional.ofNullable(regex)
                .filter(r -> !r.isEmpty())
                .map(r -> (ArgumentValidator) argument -> Optional.ofNullable(argument)
                        .map(arg -> arg.matches(r))
                        .orElse(false))
                .orElseThrow(() -> new IllegalArgumentException("Regex must not be null or empty"));
    }
}
