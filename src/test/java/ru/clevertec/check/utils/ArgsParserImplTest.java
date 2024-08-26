package ru.clevertec.check.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArgsParserImpl Tests")
class ArgsParserImplTest {

    private ArgsParser parser = ArgsParserImpl.INSTANCE;
    private final RuntimeException badRequestException = CustomExceptionFactory.createException(CustomExceptionType.BAD_REQUEST);

    @BeforeEach
    void setUp() {
        ArgsParserImpl.INSTANCE.reset();
    }

    @Test
    @DisplayName("Test parsing valid arguments")
    void testParsingValidArguments() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "1-2",
                "3-4",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=secret"
        };

        parser.parse(args);

        assertAll(
                () -> assertEquals(1234, parser.getDiscountCard()),
                () -> assertEquals(100.50, parser.getBalanceDebitCard()),
                () -> {
                    List<String[]> productsList = parser.getProductsList();
                    assertEquals(2, productsList.size());
                    assertArrayEquals(new String[]{"1", "2"}, productsList.get(0));
                    assertArrayEquals(new String[]{"3", "4"}, productsList.get(1));
                },
                () -> assertEquals("result.csv", parser.getSaveToFilePath()),
                () -> assertEquals("jdbc:postgresql://localhost:5432/mydb", parser.getDatasourceUrl()),
                () -> assertEquals("user", parser.getDatasourceUsername()),
                () -> assertEquals("secret", parser.getDatasourcePassword())
        );
    }

    @Test
    @DisplayName("Test parsing invalid discount card argument")
    void testParsingInvalidDiscountCardArgument() {
        String[] args = {"discountCard=abcd"};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid balance debit card argument")
    void testParsingInvalidBalanceDebitCardArgument() {
        String[] args = {"balanceDebitCard=abc"};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid product argument")
    void testParsingInvalidProductArgument() {
        String[] args = {"1-abc"};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid save to file argument")
    void testParsingInvalidSaveToFileArgument() {
        String[] args = {"saveToFile=result.txt"};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid datasource URL argument")
    void testParsingInvalidDatasourceUrlArgument() {
        String[] args = {"datasource.url=invalid_url"};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid datasource username argument")
    void testParsingInvalidDatasourceUsernameArgument() {
        String[] args = {"datasource.username="};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing invalid datasource password argument")
    void testParsingInvalidDatasourcePasswordArgument() {
        String[] args = {"datasource.password="};

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test parsing missing required arguments")
    void testParsingMissingRequiredArguments() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test missing products list")
    void testMissingProductsList() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=secret"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test missing balance debit card")
    void testMissingBalanceDebitCard() {
        String[] args = {
                "discountCard=1234",
                "1-2",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=secret"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test missing datasource URL")
    void testMissingDatasourceUrl() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "1-2",
                "saveToFile=result.csv",
                "datasource.username=user",
                "datasource.password=secret"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test missing datasource username")
    void testMissingDatasourceUsername() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "1-2",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.password=secret"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }

    @Test
    @DisplayName("Test missing datasource password")
    void testMissingDatasourcePassword() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=100.50",
                "1-2",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user"
        };

        assertThrows(badRequestException.getClass(), () -> parser.parse(args));
    }
}
