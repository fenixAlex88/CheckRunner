package ru.clevertec.check.model;

import ru.clevertec.check.utils.Formatter;
import ru.clevertec.check.utils.FormatterImpl;

public class CheckItem {
    private final String productDescription;
    private final int quantity;
    private final double price;
    private final double total;
    private final double discount;

    private CheckItem(Builder builder) {
        this.productDescription = builder.product.getDescription();
        this.quantity = builder.quantity;
        this.price = builder.product.getPrice();
        this.discount = (builder.product.getPrice() * builder.quantity * builder.discountPercentage) / 100;
        this.total = builder.product.getPrice() * builder.quantity - this.discount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        Formatter priceFormatter = FormatterImpl.PRICE;
        return ". " + productDescription + '\n' +
                priceFormatter.format(price) + " x " + quantity + "................." + priceFormatter.format(total);
    }

    public static class Builder {
        private Product product;
        private int quantity;
        private int discountPercentage;

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }


        public Builder setDiscountPercentage(int discountPercentage) {
            this.discountPercentage = discountPercentage;
            return this;
        }

        public CheckItem build() {
            if (product.isWholesale() && quantity >= 5) {
                this.discountPercentage = 10;
            }
            return new CheckItem(this);
        }
    }
}
