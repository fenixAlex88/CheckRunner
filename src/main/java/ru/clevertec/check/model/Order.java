package ru.clevertec.check.model;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Map<Integer, Integer> productQuantities;
    private String discountCardNumber;
    private double balanceDebitCard;

    public Order() {
        productQuantities = new HashMap<>();
    }

    public Map<Integer, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Integer, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public String getDiscountCardNumber() {
        return discountCardNumber;
    }

    public void setDiscountCardNumber(String discountCardNumber) {
        this.discountCardNumber = discountCardNumber;
    }

    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(double balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    public void addProduct(int productId, int quantity) {
        if (productQuantities.containsKey(productId))
            productQuantities.put(productId, productQuantities.get(productId) + quantity);
        else
            productQuantities.put(productId, quantity);
    }
}
