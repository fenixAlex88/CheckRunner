package ru.clevertec.check.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public enum FormatType implements FormatterType {
    PRICE {
        private final DecimalFormat priceFormat = new DecimalFormat(FormatterType.PRICE_FORMAT);

        @Override
        public String format(Object object) {
            if (object instanceof Double) {
                return priceFormat.format(object) + "$";
            }
            throw new IllegalArgumentException("Unsupported object type");
        }
    },
    DATE {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(FormatterType.DATE_FORMAT);

        @Override
        public String format(Object object) {
            if (object instanceof LocalDate) {
                return ((LocalDate) object).format(dateFormatter);
            }
            throw new IllegalArgumentException("Unsupported object type");
        }
    },
    TIME {
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(FormatterType.TIME_FORMAT);

        @Override
        public String format(Object object) {
            if (object instanceof LocalTime) {
                return ((LocalTime) object).format(timeFormatter);
            }
            throw new IllegalArgumentException("Unsupported object type");
        }
    };

    public abstract String format(Object object);
}
