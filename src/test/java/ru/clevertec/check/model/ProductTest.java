package ru.clevertec.check.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductBuilderWithValidData() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(50.00)
                .setWholesale(true)
                .setQuantityInStock(100)
                .build();

        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getDescription());
        assertEquals(50.00, product.getPrice());
        assertTrue(product.isWholesale());
        assertEquals(100, product.getQuantityInStock());
    }

    @Test
    public void testProductBuilderWithoutDescription() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new Product.Builder()
                .setId(1)
                .setPrice(50.00)
                .setWholesale(true)
                .setQuantityInStock(100)
                .build());

        assertEquals("Описание и цена должны быть указаны", exception.getMessage());
    }

    @Test
    public void testProductBuilderWithZeroPrice() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(0)
                .setWholesale(true)
                .setQuantityInStock(100)
                .build());

        assertEquals("Описание и цена должны быть указаны", exception.getMessage());
    }

    @Test
    public void testProductBuilderWithNegativePrice() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(-10.00)
                .setWholesale(true)
                .setQuantityInStock(100)
                .build());

        assertEquals("Описание и цена должны быть указаны", exception.getMessage());
    }
}
