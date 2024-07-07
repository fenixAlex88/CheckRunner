package ru.clevertec.check.services;

import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.CSVWorker;
import ru.clevertec.check.utils.Formatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckService {
    private static String[] generateDateTimeLine() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return new String[]{
                Formatter.formatDate(date),
                Formatter.formatTime(time)
        };
    }

    private static String[] generateProductLine(int quantity, String productDescription, double cost, double discountAmount, double withDiscount) {
        return new String[]{
                String.valueOf(quantity),
                productDescription,
                Formatter.formatPrice(cost),
                Formatter.formatPrice(discountAmount),
                Formatter.formatPrice(withDiscount)
        };
    }

    private static String[] generateTotalsLine(double totalPrice, double totalDiscount, double totalWithDiscount) {
        return new String[]{
                Formatter.formatPrice(totalPrice),
                Formatter.formatPrice(totalDiscount),
                Formatter.formatPrice(totalWithDiscount)
        };
    }

    private static String[] generateDiscountInfoLine(String cardNumber, int discountRate) {
        return new String[]{cardNumber, discountRate + "%"};
    }

    public static List<String[]> generateCheck(Order order, Map<Integer, Product> products,
                                               int discountCardRate) {
        List<String[]> checkData = new ArrayList<>();
        final String[] EmptyLine = new String[]{};
        final String[] dateTimeHeaders = new String[]{"Date", "Time"};
        final String[] productsHeaders = new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        final String[] discountInfoHeaders = new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        final String[] totalsHeaders = new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};
        double totalPrice = 0;
        double totalDiscount = 0;
        double totalWithDiscount = 0;

        checkData.add(dateTimeHeaders);
        checkData.add(generateDateTimeLine());
        checkData.add(EmptyLine);

        checkData.add(productsHeaders);
        for (Map.Entry<Integer, Integer> entry : order.getProductQuantities().entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = products.get(productId);
            String productDescription = product.getDescription();
            double discountRate = product.isWholesale() && quantity >= 5 ? 10 : discountCardRate;
            double cost = product.getPrice() * quantity;
            double discountAmount = cost * discountRate / 100;
            double withDiscount = cost - discountAmount;
            String[] productLine = generateProductLine(quantity, productDescription, cost, discountAmount, withDiscount);
            checkData.add(productLine);

            totalPrice += cost;
            totalDiscount += discountAmount;
            totalWithDiscount += withDiscount;
        }

        if (order.getDiscountCardNumber() != null && !order.getDiscountCardNumber().isEmpty()) {
            String discountCardNumber = order.getDiscountCardNumber();
            checkData.add(EmptyLine);
            checkData.add(discountInfoHeaders);
            checkData.add(generateDiscountInfoLine(discountCardNumber, discountCardRate));
        }
        checkData.add(EmptyLine);
        checkData.add(totalsHeaders);
        checkData.add(generateTotalsLine(totalPrice, totalDiscount, totalWithDiscount));

        return checkData;
    }
    public static void printCheckToConsole(List<String[]> checkData) {
        for (String[] line : checkData) {
            System.out.println(String.join(";", line));
        }
    }

    public static void saveCheckToCSV(List<String[]> checkData, String filePath) {
        CSVWorker.writeCSV(filePath, checkData, ";");
    }
}
