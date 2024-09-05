package ru.clevertec.check.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Formatter Tests")
public class FormatterImplTest {
    private final Formatter dateFormatter = FormatterImpl.DATE;
    private final Formatter timeFormatter = FormatterImpl.TIME;
    private final Formatter priceFormatter = FormatterImpl.PRICE;
    private final Formatter discountFormatter = FormatterImpl.DISCOUNT;
    private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

    @Test
    @DisplayName("Test PRICE Formatter")
    void testPriceFormatter() {
        assertAll(
                () -> assertEquals("123,45$", priceFormatter.format(123.45)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> priceFormatter.format("invalid")),
                () -> assertThrows(internalServerErrorException.getClass(), () -> priceFormatter.format(null)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> priceFormatter.format(""))
        );
    }

    @Test
    @DisplayName("Test DATE Formatter")
    void testDateFormatter() {
        assertAll(
                () -> assertEquals("2024-08-26", dateFormatter.format(LocalDate.of(2024, 8, 26))),
                () -> assertThrows(internalServerErrorException.getClass(), () -> dateFormatter.format("invalid")),
                () -> assertThrows(internalServerErrorException.getClass(), () -> dateFormatter.format(null)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> dateFormatter.format(""))
        );
    }

    @Test
    @DisplayName("Test TIME Formatter")
    void testTimeFormatter() {
        assertAll(
                () -> assertEquals("12:34:56", timeFormatter.format(LocalTime.of(12, 34, 56))),
                () -> assertThrows(internalServerErrorException.getClass(), () -> timeFormatter.format("invalid")),
                () -> assertThrows(internalServerErrorException.getClass(), () -> timeFormatter.format(null)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> timeFormatter.format(""))
        );
    }

    @Test
    @DisplayName("Test DISCOUNT Formatter")
    void testDiscountFormatter() {
        assertAll(
                () -> assertEquals("10%", discountFormatter.format(10)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> discountFormatter.format("invalid")),
                () -> assertThrows(internalServerErrorException.getClass(), () -> discountFormatter.format(null)),
                () -> assertThrows(internalServerErrorException.getClass(), () -> discountFormatter.format(""))
        );
    }
}
