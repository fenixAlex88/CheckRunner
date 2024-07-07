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

    @Override
    public String toString() {
        return "Order{" +
                "productQuantities=" + productQuantities +
                ", discountCardNumber='" + discountCardNumber + '\'' +
                ", balanceDebitCard=" + balanceDebitCard +
                '}';
    }

    public static Order parseArgumentsToOrder(String[] arguments) {
        Order order = new Order();
        for (String arg : arguments) {
            if (arg.contains("-")) {
                String[] parts = arg.split("-");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                order.addProduct(productId, quantity);
            } else if (arg.startsWith("discountCard=")) {
                String cardNumber = arg.substring("discountCard=".length());
                order.setDiscountCardNumber(cardNumber);
            } else if (arg.startsWith("balanceDebitCard=")) {
                double balance = Double.parseDouble(arg.substring("balanceDebitCard=".length()));
                order.setBalanceDebitCard(balance);
            }
        }
        return order;
    }
}
