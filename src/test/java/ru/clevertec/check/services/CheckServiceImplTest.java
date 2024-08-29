package ru.clevertec.check.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckServiceImpl Tests")
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
    @DisplayName("Test generateCheck method success")
    public void testGenerateCheckSuccess() {
        checkService.generateCheck();

        verify(productService, times(2)).getProductById(1);
        verify(productService, times(1)).getProductById(2);
        verify(discountCardService, times(1)).getDiscountCardByNumber(1111);
    }

    @Test
    @DisplayName("Test generateCheck method with not enough money")
    public void testGenerateCheckNotEnoughMoney() {
        argsParser.parse(new String[]{
                "balanceDebitCard=10.0"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.NOT_ENOUGH_MONEY).getClass(), () -> checkService.generateCheck());
    }

    @Test
    @DisplayName("Test generateCheck method with insufficient stock quantity")
    public void testGenerateCheckNotQuantityInStock() {
        argsParser.parse(new String[]{
                "1-60"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST).getClass(), () -> checkService.generateCheck());
    }

    @Test
    @DisplayName("Test generateCheck method with non-existent product")
    public void testGenerateCheckProductNotExist() {
        argsParser.parse(new String[]{
                "7-7"
        });
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        assertThrows(CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST).getClass(), () -> checkService.generateCheck());
    }

    @Test
    @DisplayName("Test saveCheckToCSV method")
    public void testSaveCheckToCSV() {
        when(discountCardService.getDiscountCardByNumber(1111)).thenReturn(discountCard);
        when(productService.getProductById(1)).thenReturn(product1);

        checkService.generateCheck();

        doNothing().when(csvWorker).writeToCSV(eq("test.csv"), anyList(), eq(";"));

        checkService.saveCheckToCSV("test.csv");

        ArgumentCaptor<List<String[]>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(csvWorker, times(1)).writeToCSV(eq("test.csv"), captor.capture(), eq(";"));

        List<String[]> capturedList = captor.getValue();
        assertNotNull(capturedList);
        assertFalse(capturedList.isEmpty());

        String[] expectedProductLine1 = {"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        String[] expectedProductLine2 = {"6", "Test Product 1", "5,00$", "3,00$", "27,00$"};
        String[] expectedProductLine3 = {"3", "Test Product 2", "50,00$", "7,50$", "142,50$"};
        String[] expectedDiscountLine1 = {"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
        String[] expectedDiscountLine2 = {"1111", "5%"};
        String[] expectedTotalLine1 = {"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};
        String[] expectedTotalLine2 = {"180,00$", "10,50$", "169,50$"};

        assertEquals(12, capturedList.size(), "Captured list should contain one element");
        assertArrayEquals(expectedProductLine1, capturedList.get(3));
        assertArrayEquals(expectedProductLine2, capturedList.get(4));
        assertArrayEquals(expectedProductLine3, capturedList.get(5));
        assertArrayEquals(expectedDiscountLine1, capturedList.get(7));
        assertArrayEquals(expectedDiscountLine2, capturedList.get(8));
        assertArrayEquals(expectedTotalLine1, capturedList.get(10));
        assertArrayEquals(expectedTotalLine2, capturedList.get(11));
    }

    @Test
    @DisplayName("Test printCheckToConsole method")
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