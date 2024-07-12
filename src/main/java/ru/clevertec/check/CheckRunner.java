package ru.clevertec.check;

import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.database.DatabaseConnectionImpl;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.DiscountCardRepositoryImpl;
import ru.clevertec.check.repository.ProductRepositoryImpl;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.services.*;
import ru.clevertec.check.utils.ArgsParser;
import ru.clevertec.check.utils.ArgsParserImpl;
import ru.clevertec.check.utils.CSVWorker;
import ru.clevertec.check.utils.CSVWorkerImpl;

public class CheckRunner {
    public static final String DEFAULT_RESULT_CSV = "./result.csv";

    public static void main(String[] args) {
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
//        try {
            argsParser.parse(args);
            CheckService checkService = getCheckService(argsParser);
            checkService.printCheckToConsole();
  /*      } catch (Exception e) {
            System.err.println(e.getMessage());
            CSVWorker csvWorker = new CSVWorkerImpl();
            csvWorker.writeErrorToCSV(e.getMessage(), argsParser.getSaveToFilePath());
        }
  */  }

    private static CheckService getCheckService(ArgsParser argsParser) {
        DatabaseConnection dbConnection = DatabaseConnectionImpl.getInstance();
        ProductRepository productRepository = new ProductRepositoryImpl(dbConnection);
        DiscountCardRepository discountCardRepository = new DiscountCardRepositoryImpl(dbConnection);
        ProductService productService = new ProductServiceDBImpl(productRepository);
        DiscountCardService discountCardService = new DiscountCardServiceDBImpl(discountCardRepository);
        CheckService checkService = new CheckServiceImpl(productService, discountCardService);

        checkService.generateCheck();

        checkService.saveCheckToCSV(argsParser.getSaveToFilePath());
        return checkService;
    }
}
