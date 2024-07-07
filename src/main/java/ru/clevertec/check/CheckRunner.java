package ru.clevertec.check;

import ru.clevertec.check.model.Order;

public class CheckRunner {
    public static void main(String[] args) {
        Order order = Order.parseArgumentsToOrder(args);
        System.out.println(order);
    }

}
