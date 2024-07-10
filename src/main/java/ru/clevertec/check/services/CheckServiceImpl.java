package ru.clevertec.check.services;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.model.*;
import ru.clevertec.check.utils.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CheckServiceImpl implements CheckService {
    private final RuntimeException badRequestException = CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST);
    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final List<ProductItem> productItems = new ArrayList<>();
    private final CSVWorker csvWorker = new CSVWorkerImpl();

    private DiscountCard discountCard;
    private double balanceDebitCard;
    private Check check;

    public CheckServiceImpl(ProductService productService, DiscountCardService discountCardService) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    private void addProductItem(int productId, int productQuantity) {
        Product product = productService.getProductById(productId);
        if (product == null || product.getQuantityInStock() < productQuantity) {
            throw badRequestException;
        }
        ProductItem existingItem = productItems.stream()
                .filter(p -> p.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setProductQuantity(existingItem.getProductQuantity() + productQuantity);
        } else {
            productItems.add(new ProductItem(product, productQuantity));
        }
    }

    @Override
    public void generateCheck () {
        RuntimeException notEnoughMoneyException = CustomExceptionFactory.createException(CustomExceptionType.NOT_ENOUGH_MONEY);
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
        discountCard = discountCardService.getDiscountCardByNumber(argsParser.getDiscountCard());
        balanceDebitCard = argsParser.getBalanceDebitCard();
        List<String[]> productsList = argsParser.getProductsList();
        for (String[] products : productsList) {
            int productId = Integer.parseInt(products[0]);
            int productQuantity = Integer.parseInt(products[1]);
            addProductItem(productId, productQuantity);
        }
        check = new Check.Builder().setProductItems(productItems).setDiscountCard(discountCard).build();
        if (check.getTotalWithDiscount() > balanceDebitCard)
            throw notEnoughMoneyException;
    }

    @Override
    public void saveCheckToCSV(String filePath) {
        List<String[]> checkData = new ArrayList<>();
        final String[] dateTimeHeaders = new String[]{"Date", "Time"};
        final String[] productsHeaders = new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        final String[] discountInfoHeaders = new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        final String[] totalsHeaders = new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};

        checkData.add(dateTimeHeaders);
        checkData.add(new String[]{Formatter.DATE.format(LocalDate.now()),
                Formatter.TIME.format(LocalTime.now())});
        checkData.add(new String[0]);
        checkData.add(productsHeaders);
        for (CheckItem checkItem : check.getItems()) {
            checkData.add(new String[]{String.valueOf(checkItem.getQuantity()),
                    checkItem.getProductDescription(),
                    Formatter.PRICE.format(checkItem.getPrice()),
                    Formatter.PRICE.format(checkItem.getDiscount()),
                    Formatter.PRICE.format(checkItem.getTotal())});
        }
        if (discountCard != null) {
            checkData.add(new String[0]);
            checkData.add(discountInfoHeaders);
            checkData.add(new String[]{String.valueOf(discountCard.getNumber()),
                    String.valueOf(discountCard.getAmount())});
        }
        checkData.add(new String[0]);
        checkData.add(totalsHeaders);
        checkData.add(new String[]{Formatter.PRICE.format(check.getTotalPrice()),
                Formatter.PRICE.format(check.getTotalDiscount()),
                Formatter.PRICE.format(check.getTotalWithDiscount())});
        csvWorker.writeToCSV(filePath, checkData, ";");
    }

    @Override
    public void printCheckToConsole() {
        System.out.println("-------------CHECK-------------");
        int index = 1;
        for (CheckItem checkItem : check.getItems()) {
            System.out.println(index++ + checkItem.toString());
        }
        System.out.println();
        System.out.println("Total.................... " + Formatter.PRICE.format(check.getTotalPrice()));
        System.out.println("Discount................. " + Formatter.PRICE.format(check.getTotalDiscount()));
        System.out.println("Total sum with discount.. " + Formatter.PRICE.format(check.getTotalWithDiscount()));
        System.out.println("Current balance.......... " + Formatter.PRICE.format(balanceDebitCard - check.getTotalWithDiscount()));
    }

}
