package ru.clevertec.check;

import ru.clevertec.check.services.*;
import ru.clevertec.check.utils.ArgsParser;
import ru.clevertec.check.utils.ArgsParserImpl;
import ru.clevertec.check.utils.CSVWorker;
import ru.clevertec.check.utils.CSVWorkerImpl;

public class CheckRunner {
    private static final String DISCOUNT_CARDS_CSV = "./src/main/resources/discountCards.csv";
    public static final String DEFAULT_RESULT_CSV = "./result.csv";

    public static void main(String[] args) {
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
        try {
            argsParser.parse(args);
            ProductService productService = new ProductServiceImpl(argsParser.getPathToFilePath());
            DiscountCardService discountCardService = new DiscountCardServiceImpl(DISCOUNT_CARDS_CSV);
            CheckService checkService = new CheckServiceImpl(productService, discountCardService);

            checkService.generateCheck();

            checkService.saveCheckToCSV(argsParser.getSaveToFilePath());
            checkService.printCheckToConsole();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            CSVWorker csvWorker = new CSVWorkerImpl();
            csvWorker.writeErrorToCSV(e.getMessage(), argsParser.getSaveToFilePath());
        }
    }
}
