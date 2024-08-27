package ru.clevertec.check.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckTest {

    @Test
    public void testCheckBuilderWithValidData() {
        Product product = new Product.Builder().setId(1).setDescription("Test Product").setQuantityInStock(10).setPrice(50.0).build();
        ProductItem productItem = new ProductItem(product, 2);
        List<ProductItem> productItems = new ArrayList<>();
        productItems.add(productItem);

        DiscountCard discountCard = new DiscountCard.Builder().setId(1).setNumber(1111).setAmount(5).build();

        Check check = new Check.Builder()
                .setProductItems(productItems)
                .setDiscountCard(discountCard)
                .build();

        assertEquals(1, check.getItems().size());
        assertEquals(100.00, check.getTotalPrice());
        assertEquals(5.00, check.getTotalDiscount());
        assertEquals(95.00, check.getTotalWithDiscount());
    }

    @Test
    public void testCheckBuilderWithoutDiscountCard() {
        Product product = new Product.Builder().setId(1).setDescription("Test Product").setQuantityInStock(10).setPrice(50.0).build();
        ProductItem productItem = new ProductItem(product, 2);
        List<ProductItem> productItems = new ArrayList<>();
        productItems.add(productItem);

        Check check = new Check.Builder()
                .setProductItems(productItems)
                .build();

        assertEquals(1, check.getItems().size());
        assertEquals(100.00, check.getTotalPrice());
        assertEquals(0.00, check.getTotalDiscount());
        assertEquals(100.00, check.getTotalWithDiscount());
    }

    @Test
    public void testCheckBuilderWithEmptyProductItems() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new Check.Builder().build());

        assertEquals("Product items must be set before building.", exception.getMessage());
    }
}

