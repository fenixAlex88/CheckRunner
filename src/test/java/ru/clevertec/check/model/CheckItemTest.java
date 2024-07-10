package ru.clevertec.check.model;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.utils.Formatter;
import ru.clevertec.check.utils.FormatterImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckItemTest {
    @Test
    void builder_Should_Calculate_Discount_Correctly() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(100.0)
                .setWholesale(true)
                .build();
        int quantity = 5;
        int discountPercentage = 10;

        CheckItem checkItem = new CheckItem.Builder()
                .setProduct(product)
                .setQuantity(quantity)
                .setDiscountPercentage(discountPercentage)
                .build();

        assertEquals(50.0, checkItem.getDiscount());
        assertEquals(450.0, checkItem.getWithDiscount());
    }

    @Test
    void builder_Should_Apply_Wholesale_Discount_When_Quantity_Greater_Than_5() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(100.0)
                .setWholesale(true)
                .build();
        int quantity = 6;

        CheckItem checkItem = new CheckItem.Builder()
                .setProduct(product)
                .setQuantity(quantity)
                .build();

        assertEquals(60.0, checkItem.getDiscount());
        assertEquals(540.0, checkItem.getWithDiscount());
    }

    @Test
    void builder_Should_Not_Apply_Wholesale_Discount_When_Quantity_Less_Than_5() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(100.0)
                .setWholesale(true)
                .build();
        int quantity = 4;

        CheckItem checkItem = new CheckItem.Builder()
                .setProduct(product)
                .setQuantity(quantity)
                .build();

        assertEquals(0.0, checkItem.getDiscount());
        assertEquals(400.0, checkItem.getWithDiscount());
    }

    @Test
    void toString_Should_Return_FormattedString_With_ProductDescription_And_Prices() {
        int quantity = 2;
        String productDescription = "Test Product";
        double price = 100.0;
        double withDiscount = 180.0;
        Formatter priceFormatter = FormatterImpl.PRICE;

        CheckItem checkItem = new CheckItem.Builder()
                .setProduct(new Product.Builder()
                        .setId(1)
                        .setDescription(productDescription)
                        .setPrice(price)
                        .setWholesale(true)
                        .build())
                .setQuantity(quantity)
                .setDiscountPercentage(10)
                .build();

        String result = checkItem.toString();

        String expected = ". " + productDescription + "\n" +
                priceFormatter.format(price) + " x " + quantity + "................." + priceFormatter.format(withDiscount);
        assertEquals(expected, result);
    }
}