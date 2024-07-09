package ru.clevertec.check.model;

public class DiscountCard {
    private final int id;
    private final int number;
    private final int amount;

    private DiscountCard(Builder builder) {
        this.id = builder.id;
        this.number = builder.number;
        this.amount = builder.amount;
    }

    public static class Builder {
        private int id;
        private int number;
        private int amount;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public DiscountCard build() {
            return new DiscountCard(this);
        }
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getAmount() {
        return amount;
    }
}
