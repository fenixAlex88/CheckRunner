package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public enum Formatter implements IFormatter {
    PRICE {
        private final DecimalFormat priceFormat = new DecimalFormat(IFormatter.PRICE_FORMAT);
        private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

        @Override
        public String format(Object object) {
            if (object instanceof Double) {
                return priceFormat.format(object) + "$";
            }
            throw internalServerErrorException;
        }
    },
    DATE {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(IFormatter.DATE_FORMAT);
        private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

        @Override
        public String format(Object object) {
            if (object instanceof LocalDate) {
                return ((LocalDate) object).format(dateFormatter);
            }
            throw internalServerErrorException;
        }
    },
    TIME {
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(IFormatter.TIME_FORMAT);
        private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

        @Override
        public String format(Object object) {
            if (object instanceof LocalTime) {
                return ((LocalTime) object).format(timeFormatter);
            }
            throw internalServerErrorException;
        }
    },
    DISCOUNT {
        private final String discountFormat = IFormatter.DISCOUNT_PERCENTAGE_FORMAT;
        private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

        @Override
        public String format(Object object) {
            if (object instanceof Integer) {
                return object + "%";
            }
            throw internalServerErrorException;
        }
    };

    public abstract String format(Object object);
}
