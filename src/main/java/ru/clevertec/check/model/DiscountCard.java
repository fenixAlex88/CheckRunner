package ru.clevertec.check.model;

public class DiscountCard {
    private int id;
    private String cardNumber;
    private double discountAmount;

    public DiscountCard(int id, String cardNumber, double discountAmount) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.discountAmount = discountAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
