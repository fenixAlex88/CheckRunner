package ru.clevertec.check.model;

import ru.clevertec.check.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Map<Integer, Integer> productQuantities;
    private String discountCardNumber;
    private double balanceDebitCard;

    private Order() {
        productQuantities = new HashMap<>();
    }

    public Order(double balanceDebitCard, Map<Integer, Integer> productQuantities) {
        this.balanceDebitCard = balanceDebitCard;
        this.productQuantities = productQuantities;
    }

    public Order(double balanceDebitCard, String discountCardNumber, Map<Integer, Integer> productQuantities) {
        this.balanceDebitCard = balanceDebitCard;
        this.discountCardNumber = discountCardNumber;
        this.productQuantities = productQuantities;
    }

    public Map<Integer, Integer> getProductQuantities() {
        return productQuantities;
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

    public static Order parseArgumentsToOrder(String[] arguments) throws BadRequestException {
        Order order = new Order();
        boolean hasProducts = false;
        boolean hasBalance = false;
        for (String arg : arguments) {
            if (arg.contains("-") && !arg.startsWith("balanceDebitCard")) {
                String[] parts = arg.split("-");
                if (!parts[0].matches("\\d+") || !parts[1].matches("\\d+"))
                    throw new BadRequestException();
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                order.addProduct(productId, quantity);
                hasProducts = true;
            } else if (arg.startsWith("discountCard=")) {
                String cardNumber = arg.substring("discountCard=".length());
                if (!cardNumber.matches("\\d{4}")) {
                    throw new BadRequestException();
                }
                order.setDiscountCardNumber(cardNumber);
            } else if (arg.startsWith("balanceDebitCard=")) {
                String balanceStr = (arg.substring("balanceDebitCard=".length()));
                if (!balanceStr.matches("-?\\d+(\\.\\d{1,2}?)")) {
                    throw new BadRequestException();
                }
                double balance = Double.parseDouble(balanceStr);
                order.setBalanceDebitCard(balance);
                hasBalance = true;
            }
        }
        if (!hasProducts || !hasBalance) {
            throw new BadRequestException();
        }
        return order;
    }
}
