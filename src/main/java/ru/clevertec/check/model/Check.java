package ru.clevertec.check.model;

import java.util.ArrayList;
import java.util.List;

public class Check {
    private final List<CheckItem> items;
    private final double totalPrice;
    private final double totalDiscount;
    private final double totalWithDiscount;

    private Check(Builder builder) {
        this.items = builder.checkItems;
        this.totalPrice = builder.totalPrice;
        this.totalDiscount = builder.totalDiscount;
        this.totalWithDiscount = builder.totalWithDiscount;
    }

    public List<CheckItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public static class Builder {
        private List<ProductItem> productItems;
        private final List<CheckItem> checkItems = new ArrayList<>();
        private DiscountCard discountCard;
        private double totalPrice = 0;
        private double totalDiscount = 0;
        private double totalWithDiscount = 0;

        public Builder setProductItems(List<ProductItem> productItems) {
            this.productItems = productItems;
            return this;
        }

        public Builder setDiscountCard(DiscountCard discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public Check build() {
            if (productItems == null) {
                throw new IllegalStateException("Product items must be set before building.");
            }
            int discountPercentage = discountCard == null ? 0 : discountCard.getAmount();
            for (ProductItem productItem : productItems) {
                Product product = productItem.getProduct();
                int quantity = productItem.getProductQuantity();
                CheckItem checkItem = new CheckItem.Builder()
                        .setProduct(product)
                        .setQuantity(quantity)
                        .setDiscountPercentage(discountPercentage)
                        .build();
                checkItems.add(checkItem);
                this.totalPrice += checkItem.getTotal();
                this.totalDiscount += checkItem.getDiscount();
            }
            this.totalWithDiscount = totalPrice - totalDiscount;
            return new Check(this);
        }
    }
}
