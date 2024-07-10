package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.validator.ArgumentValidator;
import ru.clevertec.check.validator.ProductArgumentValidator;
import ru.clevertec.check.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;

public enum ArgsParserImpl implements ArgsParser {
    INSTANCE;

    private int discountCart;
    private double balanceDebitCard;
    private final List<String[]> productsList = new ArrayList<>();
    private String pathToFilePath;
    private String saveToFilePath;

    @Override
    public void parse(String[] args) {
        ArgumentValidator productIdQuantityValidator = new ProductArgumentValidator();
        ArgumentValidator discountCardValidator = ValidatorFactory.createValidator(ArgumentValidator.DISCOUNT_CARD_REGEX);
        ArgumentValidator balanceDebitCardValidator = ValidatorFactory.createValidator(ArgumentValidator.BALANCE_DEBIT_CARD_REGEX);

        boolean hasProducts = false;
        boolean hasBalance = false;

        RuntimeException badRequestException = CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST);
        for (String arg : args) {
            if (arg.startsWith("discountCard=")) {
                if (!discountCardValidator.validate(arg))
                    throw badRequestException;
                discountCart = Integer.parseInt(arg.substring("discountCard=".length()));
            } else if (arg.startsWith("balanceDebitCard=")) {
                if (!balanceDebitCardValidator.validate(arg))
                    throw badRequestException;
                balanceDebitCard = Double.parseDouble(arg.substring("balanceDebitCard=".length()));
                hasBalance = true;
            } else if (arg.contains("-")) {
                if (!productIdQuantityValidator.validate(arg))
                    throw badRequestException;
                productsList.add(arg.split("-"));
                hasProducts = true;
            }
        }
        if (!hasProducts || !hasBalance) {
            throw badRequestException;
        }
    }

    @Override
    public int getDiscountCard() {
        return discountCart;
    }

    @Override
    public double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    @Override
    public List<String[]> getProductsList() {
        return productsList;
    }
}