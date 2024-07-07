package ru.clevertec.check;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountService;
import ru.clevertec.check.services.ProductService;

import java.util.List;
import java.util.Map;

public class CheckRunner {
    public static void main(String[] args) {
        Order order = Order.parseArgumentsToOrder(args);
        int discountCardRate = DiscountService.getDiscountByCardNumber(order.getDiscountCardNumber());
        Map<Integer, Product> products = ProductService.getAllProducts();
        List<String[]> checkData = CheckService.generateCheck(order, products, discountCardRate);
        CheckService.printCheckToConsole(checkData);
        CheckService.saveCheckToCSV(checkData, "./result.csv");
    }
}
