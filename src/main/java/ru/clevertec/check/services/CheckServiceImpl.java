package ru.clevertec.check.services;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.model.*;
import ru.clevertec.check.utils.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        Product product = Optional.ofNullable(productService.getProductById(productId))
                .filter(p -> p.getQuantityInStock() >= productQuantity)
                .orElseThrow(() -> badRequestException);

        productItems.stream()
                .filter(p -> p.getProduct().getId() == productId)
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> existingItem.setProductQuantity(existingItem.getProductQuantity() + productQuantity),
                        () -> productItems.add(new ProductItem(product, productQuantity))
                );
    }

    @Override
    public void generateCheck() {
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
        discountCard = discountCardService.getDiscountCardByNumber(argsParser.getDiscountCard());
        balanceDebitCard = argsParser.getBalanceDebitCard();
        List<String[]> productsList = argsParser.getProductsList();

        productsList.forEach(products -> {
            int productId = Integer.parseInt(products[0]);
            int productQuantity = Integer.parseInt(products[1]);
            addProductItem(productId, productQuantity);
        });

        check = new Check.Builder()
                .setProductItems(productItems)
                .setDiscountCard(discountCard)
                .build();

        Optional.of(check)
                .filter(c -> c.getTotalWithDiscount() <= balanceDebitCard)
                .orElseThrow(() -> CustomExceptionFactory.createException(CustomExceptionType.NOT_ENOUGH_MONEY));
    }

    @Override

    public void saveCheckToCSV(String filePath) {
        List<String[]> checkData = new ArrayList<>();
        final Formatter dateFormatter = FormatterImpl.DATE;
        final Formatter timeFormatter = FormatterImpl.TIME;
        final Formatter priceFormatter = FormatterImpl.PRICE;
        final Formatter discountFormatter = FormatterImpl.DISCOUNT;

        checkData.add(new String[]{"Date", "Time"});
        checkData.add(new String[]{dateFormatter.format(LocalDate.now()), timeFormatter.format(LocalTime.now())});

        checkData.add(new String[0]);
        checkData.add(new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"});
        check.getItems().forEach(checkItem -> checkData.add(new String[]{
                String.valueOf(checkItem.getQuantity()),
                checkItem.getProductDescription(),
                priceFormatter.format(checkItem.getPrice()),
                priceFormatter.format(checkItem.getDiscount()),
                priceFormatter.format(checkItem.getWithDiscount())
        }));

        Optional.ofNullable(discountCard).ifPresent(card -> {
            checkData.add(new String[0]);
            checkData.add(new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"});
            checkData.add(new String[]{String.valueOf(card.getNumber()), discountFormatter.format(card.getAmount())});
        });

        checkData.add(new String[0]);
        checkData.add(new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"});
        checkData.add(new String[]{
                priceFormatter.format(check.getTotalPrice()),
                priceFormatter.format(check.getTotalDiscount()),
                priceFormatter.format(check.getTotalWithDiscount())
        });

        csvWorker.writeToCSV(filePath, checkData, ";");
    }

    @Override
    public void printCheckToConsole() {
        Formatter priceFormatter = FormatterImpl.PRICE;
        System.out.println("-------------CHECK-------------");
        IntStream.range(0, check.getItems().size())
                .mapToObj(i -> (i + 1) + ". " + check.getItems().get(i).toString())
                .forEach(System.out::println);
        System.out.println();
        System.out.println("Total.................... " + priceFormatter.format(check.getTotalPrice()));
        System.out.println("Discount................. " + priceFormatter.format(check.getTotalDiscount()));
        System.out.println("Total sum with discount.. " + priceFormatter.format(check.getTotalWithDiscount()));
        System.out.println("Current balance.......... " + priceFormatter.format(balanceDebitCard - check.getTotalWithDiscount()));
    }

}
