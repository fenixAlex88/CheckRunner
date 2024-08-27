package ru.clevertec.check.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.ArgsParserImpl;
import ru.clevertec.check.utils.CSVWorker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
public class CheckServiceImplTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ArgsParserImpl argsParser = ArgsParserImpl.INSTANCE;

    private DiscountCard discountCard;
    private Product product1;

    @Mock
    private ProductService productService;

    @Mock
    private DiscountCardService discountCardService;

    @Mock
    private CSVWorker csvWorker;

    @InjectMocks
    private CheckServiceImpl checkService;

    @BeforeEach
    public void setUp() {
        argsParser.reset();
        argsParser.parse(new String[]{
                "discountCard=1111",
                "balanceDebitCard=1000.0",
                "1-1",
                "1-5",
                "2-3",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=secret"
        });

        product1 = new Product.Builder()
                .setId(1)
                .setDescription("Test Product 1")
                .setPrice(5.0)
                .setWholesale(true)
                .setQuantityInStock(50)
                .build();

        Product product2 = new Product.Builder()
                .setId(2)
                .setDescription("Test Product 2")
                .setPrice(50.0)
                .setWholesale(true)
                .setQuantityInStock(5)
                .build();

        discountCard = new DiscountCard.Builder()
                .setId(1)
                .setNumber(1111)
                .setAmount(5)
                .build();

        checkService = new CheckServiceImpl(productService, discountCardService, csvWorker);

        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);
        when(productService.getProductById(2)).thenReturn(product2);
    }

    @Test
    public void testGenerateCheckSuccess() {
        checkService.generateCheck();

        verify(productService, times(2)).getProductById(1);
        verify(productService, times(1)).getProductById(2);
        verify(discountCardService, times(1)).getDiscountCardByNumber(1111);
    }

    @Test
    public void testGenerateCheckNotEnoughMoney() {
        argsParser.parse(new String[]{
                "balanceDebitCard=10.0"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.NOT_ENOUGH_MONEY).getClass(), () -> checkService.generateCheck());
    }

    @Test
    public void testGenerateCheckNotQuantityInStock() {
        argsParser.parse(new String[]{
                "1-60"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST).getClass(), () -> checkService.generateCheck());
    }

    @Test
    public void testGenerateCheckProductNotExist() {
        argsParser.parse(new String[]{
                "7-7"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST).getClass(), () -> checkService.generateCheck());
    }

    @Test
    public void testSaveCheckToCSV() {
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        checkService.generateCheck();

        doNothing().when(csvWorker).writeToCSV(eq("test.csv"), anyList(), eq(";"));

        checkService.saveCheckToCSV("test.csv");

        verify(csvWorker, times(1)).writeToCSV(eq("test.csv"), anyList(), eq(";"));
    }

    @Test
    public void testPrintCheckToConsole() {
        String expected = """
                -------------CHECK-------------
                1. Test Product 1
                5,00$ x 6.................27,00$
                2. Test Product 2
                50,00$ x 3.................142,50$

                Total.................... 180,00$
                Discount................. 10,50$
                Total sum with discount.. 169,50$
                Current balance.......... 830,50$
                """;

        System.setOut(new PrintStream(outputStreamCaptor));

        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        checkService.generateCheck();
        checkService.printCheckToConsole();

        String actualOutput = outputStreamCaptor.toString().replace("\r\n", "").replace("\n", "");
        assertEquals(expected.replace("\n", ""), actualOutput);

        System.setOut(standardOut);
    }
}