package ru.clevertec.check.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormatterTest {

    @Test
    public void testPriceFormatter() {
        Formatter formatter = FormatterImpl.PRICE;
        String result = formatter.format(123.456);
        assertEquals("123,46$", result, "Price should be formatted correctly with two decimal places and a dollar sign.");
    }

    @Test
    public void testPriceFormatterThrowsException() {
        Formatter formatter = FormatterImpl.PRICE;
        assertThrows(RuntimeException.class, () -> formatter.format("Not a number"), "Should throw an exception for non-double input.");
    }

    @Test
    public void testPriceFormatterWithRounding() {
        Formatter formatter = FormatterImpl.PRICE;
        String result = formatter.format(123.4567);
        assertEquals("123,46$", result, "Price should be rounded to two decimal places.");
    }

    @Test
    public void testPriceFormatterWithExactValue() {
        Formatter formatter = FormatterImpl.PRICE;
        String result = formatter.format(123.00);
        assertEquals("123,00$", result, "Price should be formatted with two decimal places even if it's an exact value.");
    }

    @Test
    public void testDateFormat() {
        Formatter formatter = FormatterImpl.DATE;
        LocalDate date = LocalDate.of(2024, 7, 10);
        String result = formatter.format(date);
        assertEquals("2024-07-10", result, "Date should be formatted in yyyy-MM-dd format.");
    }

    @Test
    public void testDateFormatThrowsException() {
        Formatter formatter = FormatterImpl.DATE;
        assertThrows(RuntimeException.class, () -> formatter.format("Not a date"), "Should throw an exception for non-LocalDate input.");
    }

    @Test
    public void testDateFormatWithLeapYear() {
        Formatter formatter = FormatterImpl.DATE;
        LocalDate date = LocalDate.of(2020, 2, 29);
        String result = formatter.format(date);
        assertEquals("2020-02-29", result, "Date should be formatted correctly for a leap year.");
    }

    @Test
    public void testTimeFormat() {
        Formatter formatter = FormatterImpl.TIME;
        LocalTime time = LocalTime.of(17, 30, 15);
        String result = formatter.format(time);
        assertEquals("17:30:15", result, "Time should be formatted in HH:mm:ss format.");
    }

    @Test
    public void testTimeFormatThrowsException() {
        Formatter formatter = FormatterImpl.TIME;
        assertThrows(RuntimeException.class, () -> formatter.format("Not a time"), "Should throw an exception for non-LocalTime input.");
    }

    @Test
    public void testTimeFormatWithMidnight() {
        Formatter formatter = FormatterImpl.TIME;
        LocalTime time = LocalTime.of(0, 0, 0);
        String result = formatter.format(time);
        assertEquals("00:00:00", result, "Midnight should be formatted as 00:00:00.");
    }

    @Test
    public void testDiscountFormat() {
        Formatter formatter = FormatterImpl.DISCOUNT;
        String result = formatter.format(50);
        assertEquals("50%", result, "Discount should be formatted with a percentage sign.");
    }

    @Test
    public void testDiscountFormatThrowsException() {
        Formatter formatter = FormatterImpl.DISCOUNT;
        assertThrows(RuntimeException.class, () -> formatter.format("Not an integer"), "Should throw an exception for non-integer input.");
    }

    @Test
    public void testDiscountFormatWithZero() {
        Formatter formatter = FormatterImpl.DISCOUNT;
        String result = formatter.format(0);
        assertEquals("0%", result, "Zero discount should be formatted with a percentage sign.");
    }

    @Test
    public void testDiscountFormatWithHundred() {
        Formatter formatter = FormatterImpl.DISCOUNT;
        String result = formatter.format(100);
        assertEquals("100%", result, "A hundred percent discount should be formatted correctly.");
    }
}
