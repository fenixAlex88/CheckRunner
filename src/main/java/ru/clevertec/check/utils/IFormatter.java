package ru.clevertec.check.utils;

public interface IFormatter {
    String PRICE_FORMAT = "#0.00";
    String DATE_FORMAT = "yyyy-MM-dd";
    String TIME_FORMAT = "HH:mm:ss";
    String DISCOUNT_PERCENTAGE_FORMAT = "%";

    String format(Object object);
}
