package ru.clevertec.check.model;


public class Product {
    private final int id;
    private final String description;
    private final double price;
    private final boolean isWholesale;
    private final int quantityInStock;

    private Product(Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.price = builder.price;
        this.quantityInStock = builder.quantityInStock;
        this.isWholesale = builder.isWholesale;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public static class Builder {
        private int id;
        private String description;
        private double price;
        private boolean isWholesale;
        private int quantityInStock;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setWholesale(boolean isWholesale) {
            this.isWholesale = isWholesale;
            return this;
        }

        public Builder setQuantityInStock(int quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
