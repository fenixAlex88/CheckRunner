package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.validator.ArgumentValidator;
import ru.clevertec.check.validator.ProductArgumentValidator;
import ru.clevertec.check.validator.ValidatorFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static ru.clevertec.check.CheckRunner.DEFAULT_RESULT_CSV;

public enum ArgsParserImpl implements ArgsParser {
    INSTANCE;
    private final ArgumentValidator productIdQuantityValidator = new ProductArgumentValidator();
    private final ArgumentValidator discountCardValidator = ValidatorFactory.createValidator(ArgumentValidator.DISCOUNT_CARD_REGEX);
    private final ArgumentValidator balanceDebitCardValidator = ValidatorFactory.createValidator(ArgumentValidator.BALANCE_DEBIT_CARD_REGEX);
    private final ArgumentValidator saveToFileValidator = ValidatorFactory.createValidator(ArgumentValidator.SAVE_TO_FILE_REGEX);
    private final ArgumentValidator datasourceUrlValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_URL_REGEX);
    private final ArgumentValidator datasourceUserValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_USER_REGEX);
    private final ArgumentValidator datasourcePasswordValidator = ValidatorFactory.createValidator(ArgumentValidator.DATASOURCE_PASSWORD_REGEX);

    private int discountCart;
    private double balanceDebitCard;
    private final List<String[]> productsList = new ArrayList<>();
    private String saveToFilePath;
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;
    private final RuntimeException badRequestException = CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST);

    private final Map<Predicate<String>, Consumer<String>> argumentProcessors = Map.of(
            arg -> arg.startsWith("discountCard="), this::processDiscountCard,
            arg -> arg.startsWith("balanceDebitCard="), this::processBalanceDebitCard,
            arg -> arg.contains("-"), this::processProduct,
            arg -> arg.startsWith("saveToFile="), this::processSaveToFile,
            arg -> arg.startsWith("datasource.url="), this::processDatasourceUrl,
            arg -> arg.startsWith("datasource.username="), this::processDatasourceUsername,
            arg -> arg.startsWith("datasource.password="), this::processDatasourcePassword
    );

    @Override
    public void parse(String[] args) {
        Arrays.stream(args).forEach(arg ->
                argumentProcessors.entrySet().stream()
                        .filter(entry -> entry.getKey().test(arg))
                        .findFirst()
                        .ifPresent(entry -> entry.getValue().accept(arg))
        );
        validateRequiredArguments();
    }

    private void validateRequiredArguments() {
        saveToFilePath = Optional.ofNullable(saveToFilePath).orElse(DEFAULT_RESULT_CSV);
        if (productsList.isEmpty() || balanceDebitCard == 0 || datasourceUrl == null || datasourceUsername == null || datasourcePassword == null) {
            throw badRequestException;
        }
    }

    private void processDiscountCard(String arg) {
        if (!discountCardValidator.validate(arg)) throw badRequestException;
        discountCart = Integer.parseInt(arg.substring("discountCard=".length()));
    }

    private void processBalanceDebitCard(String arg) {
        if (!balanceDebitCardValidator.validate(arg)) throw badRequestException;
        balanceDebitCard = Double.parseDouble(arg.substring("balanceDebitCard=".length()));
    }

    private void processProduct(String arg) {
        if (!productIdQuantityValidator.validate(arg)) throw badRequestException;
        productsList.add(arg.split("-"));
    }

    private void processSaveToFile(String arg) {
        if (!saveToFileValidator.validate(arg)) throw badRequestException;
        saveToFilePath = arg.substring("saveToFile=".length());
    }

    private void processDatasourceUrl(String arg) {
        if (!datasourceUrlValidator.validate(arg)) throw badRequestException;
        datasourceUrl = arg.substring("datasource.url=".length());
    }

    private void processDatasourceUsername(String arg) {
        if (!datasourceUserValidator.validate(arg)) throw badRequestException;
        datasourceUsername = arg.substring("datasource.username=".length());
    }

    private void processDatasourcePassword(String arg) {
        if (!datasourcePasswordValidator.validate(arg)) throw badRequestException;
        datasourcePassword = arg.substring("datasource.password=".length());
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

    @Override
    public String getSaveToFilePath() {
        return saveToFilePath;
    }

    @Override
    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    @Override
    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    @Override
    public String getDatasourcePassword() {
        return datasourcePassword;
    }
}
