package ru.clevertec.check;

import ru.clevertec.check.exception.InternalServerErrorException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.DiscountService;
import ru.clevertec.check.services.ProductService;
import ru.clevertec.check.utils.CSVWorker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CheckRunner {
    public static void main(String[] args) {
        try {
            Order order = Order.parseArgumentsToOrder(args);
            int discountCardRate = DiscountService.getDiscountByCardNumber(order.getDiscountCardNumber());
            Map<Integer, Product> products = ProductService.getAllProducts();
            List<String[]> checkData = CheckService.generateCheck(order, products, discountCardRate);
            CheckService.printCheckToConsole(checkData);
            CheckService.saveCheckToCSV(checkData, "./result.csv");
        } catch (NotEnoughMoneyException | BadRequestException | InternalServerErrorException e) {
            System.err.println(e.getMessage());
            CSVWorker.writeCSV("./result.csv", e.getMessage());
        } catch (Exception e) {
            System.err.println("INTERNAL SERVER ERROR");
            CSVWorker.writeCSV("./result.csv", "INTERNAL SERVER ERROR");
        }
    }
}
