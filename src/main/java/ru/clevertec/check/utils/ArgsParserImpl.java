package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.validator.ArgumentValidator;
import ru.clevertec.check.validator.ProductArgumentValidator;
import ru.clevertec.check.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.check.CheckRunner.DEFAULT_RESULT_CSV;

public enum ArgsParserImpl implements ArgsParser {
    INSTANCE;

    private int discountCart;
    private double balanceDebitCard;
    private final List<String[]> productsList = new ArrayList<>();
    private String saveToFilePath;
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;

    @Override
    public void parse(String[] args) {
        ArgumentValidator productIdQuantityValidator = new ProductArgumentValidator();
        ArgumentValidator discountCardValidator = ValidatorFactory.createValidator(ArgumentValidator.DISCOUNT_CARD_REGEX);
        ArgumentValidator balanceDebitCardValidator = ValidatorFactory.createValidator(ArgumentValidator.BALANCE_DEBIT_CARD_REGEX);
        ArgumentValidator saveToFileValidator = ValidatorFactory.createValidator(ArgumentValidator.SAVE_TO_FILE_REGEX);
        ArgumentValidator datasourceUrlValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_URL_REGEX);
        ArgumentValidator datasourceUserValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_USER_REGEX);
        ArgumentValidator datasourcePasswordValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_PASSWORD_REGEX);

        boolean hasProducts = false;
        boolean hasBalance = false;
        boolean hasSaveToFilePath = false;
        boolean hasDatasourceUrl = false;
        boolean hasDatasourceUsername = false;
        boolean hasDatasourcePassword = false;

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
            } else if (arg.startsWith("saveToFile=")) {
                if (!saveToFileValidator.validate(arg))
                    throw badRequestException;
                saveToFilePath = arg.substring("saveToFile=".length());
                hasSaveToFilePath = true;
            } else if (arg.startsWith("datasource.url=")) {
                if (!datasourceUrlValidator.validate(arg))
                    throw badRequestException;
                datasourceUrl = arg.substring("datasource.url=".length());
                hasDatasourceUrl = true;
            } else if (arg.startsWith("datasource.username=")) {
                if (!datasourceUserValidator.validate(arg))
                    throw badRequestException;
                datasourceUsername = arg.substring("datasource.username=".length());
                hasDatasourceUsername = true;
            } else if (arg.startsWith("datasource.password=")) {
                if (!datasourcePasswordValidator.validate(arg))
                    throw badRequestException;
                datasourcePassword = arg.substring("datasource.password=".length());
                hasDatasourcePassword = true;
            }
        }

            if (!hasSaveToFilePath)
                saveToFilePath = DEFAULT_RESULT_CSV;
            if (!hasProducts || !hasBalance || !hasSaveToFilePath || !hasDatasourceUrl || !hasDatasourceUsername || !hasDatasourcePassword) {
                throw badRequestException;
            }
        }

        @Override
        public int getDiscountCard () {
            return discountCart;
        }

        @Override
        public double getBalanceDebitCard () {
            return balanceDebitCard;
        }

        @Override
        public List<String[]> getProductsList () {
            return productsList;
        }

        @Override
        public String getSaveToFilePath () {
            return saveToFilePath;
        }

        @Override
        public String getDatasourceUrl () {
            return datasourceUrl;
        }

        @Override
        public String getDatasourceUsername () {
            return datasourceUsername;
        }

        @Override
        public String getDatasourcePassword () {
            return datasourcePassword;
        }
    }
