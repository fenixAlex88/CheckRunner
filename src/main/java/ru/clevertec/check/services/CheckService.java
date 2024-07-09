package ru.clevertec.check.services;

import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.CSVWorker;

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
                FormatterOld.formatDate(date),
                FormatterOld.formatTime(time)
        };
    }

    private static String[] generateProductLine(int quantity, String productDescription, double cost, double discountAmount, double withDiscount) {
        return new String[]{
                String.valueOf(quantity),
                productDescription,
                FormatterOld.formatPrice(cost),
                FormatterOld.formatPrice(discountAmount),
                FormatterOld.formatPrice(withDiscount)
        };
    }

    private static String[] generateTotalsLine(double totalPrice, double totalDiscount, double totalWithDiscount) {
        return new String[]{
                FormatterOld.formatPrice(totalPrice),
                FormatterOld.formatPrice(totalDiscount),
                FormatterOld.formatPrice(totalWithDiscount)
        };
    }

    private static String[] generateDiscountInfoLine(String cardNumber, int discountRate) {
        return new String[]{cardNumber, discountRate + "%"};
    }

    public static List<String[]> generateCheck(Order order, List<Product> products,
                                               int discountCardRate) {
        List<String[]> checkData = new ArrayList<>();
        final String[] dateTimeHeaders = new String[]{"Date", "Time"};
        final String[] productsHeaders = new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        final String[] discountInfoHeaders = new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        final String[] totalsHeaders = new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};
        double totalPrice = 0;
        double totalDiscount = 0;
        double totalWithDiscount = 0;

        checkData.add(dateTimeHeaders);
        checkData.add(generateDateTimeLine());
        checkData.add(new String[0]);

        checkData.add(productsHeaders);
        for (Map.Entry<Integer, Integer> entry : order.getProductQuantities().entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
            if (product == null || product.getQuantity() < order.getProductQuantities().get(productId))
                throw new BadRequestException();
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
        if (totalPrice > order.getBalanceDebitCard()) {
            throw new NotEnoughMoneyException();
        }

        if (order.getDiscountCardNumber() != null && !order.getDiscountCardNumber().isEmpty()) {
            String discountCardNumber = order.getDiscountCardNumber();
            checkData.add(new String[0]);
            checkData.add(discountInfoHeaders);
            checkData.add(generateDiscountInfoLine(discountCardNumber, discountCardRate));
        }
        checkData.add(new String[0]);
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
