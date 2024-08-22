package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public enum FormatterImpl implements Formatter {
    PRICE {
        private final DecimalFormat priceFormat = new DecimalFormat(Formatter.PRICE_FORMAT);

        @Override
        public String format(Object object) {
            return formatObject(object, Double.class, priceFormat::format, "$");
        }
    },
    DATE {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Formatter.DATE_FORMAT);

        @Override
        public String format(Object object) {
            return formatObject(object, LocalDate.class, dateFormatter::format, "");
        }
    },
    TIME {
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Formatter.TIME_FORMAT);

        @Override
        public String format(Object object) {
            return formatObject(object, LocalTime.class, timeFormatter::format, "");
        }
    },
    DISCOUNT {
        @Override
        public String format(Object object) {
            return formatObject(object, Integer.class, String::valueOf, Formatter.DISCOUNT_PERCENTAGE_FORMAT);
        }
    };

    private static final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

    public abstract String format(Object object);

    private static <T> String formatObject(Object object, Class<T> type, FormatterFunction<T> formatter, String suffix) {
        return Optional.ofNullable(object)
                .filter(type::isInstance)
                .map(type::cast)
                .map(formatter::apply)
                .map(result -> result + suffix)
                .orElseThrow(() -> internalServerErrorException);
    }

    @FunctionalInterface
    private interface FormatterFunction<T> {
        String apply(T t);
    }
}
