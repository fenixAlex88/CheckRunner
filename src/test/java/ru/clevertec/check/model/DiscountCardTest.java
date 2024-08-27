package ru.clevertec.check.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountCardTest {

    @Test
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
    public void testDiscountCardBuilderWithZeroNumber() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new DiscountCard.Builder()
                .setId(1)
                .setNumber(0)
                .setAmount(10)
                .build());

        assertEquals("Все поля должны быть корректно заполнены", exception.getMessage());
    }

    @Test
    public void testDiscountCardBuilderWithNegativeAmount() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new DiscountCard.Builder()
                .setId(1)
                .setNumber(1111)
                .setAmount(-5)
                .build());

        assertEquals("Все поля должны быть корректно заполнены", exception.getMessage());
    }
}
