package ru.clevertec.check.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import static org.junit.jupiter.api.Assertions.*;

public class ArgsParserTest {
    private ArgsParser argsParser;

    @BeforeEach
    void setUp() {
        argsParser = ArgsParserImpl.INSTANCE;
    }

    @Test
    void parseValidArgs() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=250.00",
                "1-5",
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=pass"
        };
        assertDoesNotThrow(() -> argsParser.parse(args));
        assertEquals(1234, argsParser.getDiscountCard());
        assertEquals(250.00, argsParser.getBalanceDebitCard(), 0.01);
        assertEquals("result.csv", argsParser.getSaveToFilePath());
        assertEquals("jdbc:postgresql://localhost:5432/mydb", argsParser.getDatasourceUrl());
        assertEquals("user", argsParser.getDatasourceUsername());
        assertEquals("pass", argsParser.getDatasourcePassword());
        assertFalse(argsParser.getProductsList().isEmpty());
    }

    @Test
    void parseInvalidDiscountCard() {
        String[] args = {"discountCard=abcd"};
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }

    @Test
    void parseInvalidBalanceDebitCard() {
        String[] args = {"balanceDebitCard=abc"};
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }

    @Test
    void parseInvalidProductArgument() {
        String[] args = {"1-a"};
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }

    @Test
    void parseMissingMandatoryArgs() {
        String[] args = {"discountCard=1234"};
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }

    @Test
    void parseWithIncompleteProductInfo() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=250.00",
                "1-", // Incomplete product info
                "saveToFile=result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/mydb",
                "datasource.username=user",
                "datasource.password=pass"
        };
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }

    @Test
    void parseWithInvalidDatasourceUrl() {
        String[] args = {
                "discountCard=1234",
                "balanceDebitCard=250.00",
                "1-5",
                "saveToFile=result.csv",
                "datasource.url=invalid_url",
                "datasource.username=user",
                "datasource.password=pass"
        };
        Executable parseCall = () -> argsParser.parse(args);
        assertThrows(RuntimeException.class, parseCall);
    }
}
