package ru.clevertec.check.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckTest {

    @Test
    void builder_Should_Calculate_TotalPrice_Correctly() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product").
                setPrice(100.0).
                setQuantityInStock(10).
                build();
        ProductItem productItem = new ProductItem(product, 2);
        List<ProductItem> productItems = List.of(productItem);

        Check check = new Check.Builder()
                .setProductItems(productItems)
                .build();

        assertEquals(200.0, check.getTotalPrice());
    }

    @Test
    void builder_Should_Apply_Discount_Correctly() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product").
                setPrice(100.0).
                setQuantityInStock(10).
                build();
        ProductItem productItem = new ProductItem(product, 3);
        List<ProductItem> productItems = List.of(productItem);
        DiscountCard discountCard = new DiscountCard.Builder().
                setNumber(1234).
                setId(1).
                setAmount(10).
                build();

        Check check = new Check.Builder()
                .setProductItems(productItems)
                .setDiscountCard(discountCard)
                .build();

        assertEquals(30.0, check.getTotalDiscount());
        assertEquals(300.0, check.getTotalPrice());
        assertEquals(270.0, check.getTotalWithDiscount());

    }

    @Test
    void builder_Should_ThrowException_When_ProductItems_Not_Set() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new Check.Builder().build();
        });

        assertEquals("Product items must be set before building.", exception.getMessage());
    }
}
