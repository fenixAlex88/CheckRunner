package ru.clevertec.check.utils;

public interface FormatterType {
    String PRICE_FORMAT = "#0.00";
    String DATE_FORMAT = "yyyy-MM-dd";
    String TIME_FORMAT = "HH:mm:ss";

    String format(Object object);
}
