package ru.clevertec.check.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Discount Card Test Suite")
public class DiscountCardTest {

    @Test
    @DisplayName("Test Discount Card Builder with Valid Data")
    public void testDiscountCardBuilderWithValidData() {
        DiscountCard discountCard = new DiscountCard.Builder()
                .setId(1)
                .setNumber(1111)
                .setAmount(10)
                .build();

        assertEquals(1111, discountCard.getNumber());
        assertEquals(10, discountCard.getAmount());
    }

    @Test
    @DisplayName("Test Discount Card Builder with Zero Number")
    public void testDiscountCardBuilderWithZeroNumber() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new DiscountCard.Builder()
                .setId(1)
                .setNumber(0)
                .setAmount(10)
                .build());

        assertEquals("Все поля должны быть корректно заполнены", exception.getMessage());
    }

    @Test
    @DisplayName("Test Discount Card Builder with Negative Amount")
    public void testDiscountCardBuilderWithNegativeAmount() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new DiscountCard.Builder()
                .setId(1)
                .setNumber(1111)
                .setAmount(-5)
                .build());

        assertEquals("Все поля должны быть корректно заполнены", exception.getMessage());
    }
}