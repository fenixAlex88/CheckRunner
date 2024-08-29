package ru.clevertec.check;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.utils.ArgsParserImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("CheckRunner Tests")
public class CheckRunnerTest {

    private final ArgsParserImpl argsParser = ArgsParserImpl.INSTANCE;

    private static final String EXPECTED_CSV_CONTENT = """
            Date;Time

            QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
            6;Cream 400g;2,71$;1,63$;14,63$
            1;Yogurt 400g;2,10$;0,06$;2,04$

            DISCOUNT CARD;DISCOUNT PERCENTAGE
            1111;3%

            TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
            18,36$;1,69$;16,67$""";

    private static final String TEST_CSV_FILE_PATH = "./test.csv";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgresql";

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        argsParser.reset();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    DROP TABLE IF EXISTS product, discount_card;
                    """);
            statement.execute("""
                    CREATE TABLE product (
                    id BIGSERIAL PRIMARY KEY,
                    description VARCHAR(50) NOT NULL,
                    price DECIMAL(5, 2) NOT NULL CHECK (price >= 0),
                    quantity_in_stock INTEGER NOT NULL CHECK (quantity_in_stock >= 0),
                    wholesale_product BOOLEAN NOT NULL
                    );
                    """);
            statement.execute("""
                    CREATE TABLE discount_card (
                    id BIGSERIAL PRIMARY KEY,
                    number INTEGER UNIQUE NOT NULL CHECK (number >= 1000 AND number < 10000),
                    amount SMALLINT NOT NULL CHECK (amount BETWEEN 0 AND 100)
                    );
                    """);
            statement.execute("""
                    CREATE INDEX idx_discount_card_number ON discount_card (number);
                    """);
            statement.execute("""
                    INSERT INTO product (id, description, price, quantity_in_stock, wholesale_product) VALUES
                    (1, 'Milk', 1.07, 10, true),
                    (2, 'Cream 400g', 2.71, 20, true),
                    (3, 'Yogurt 400g', 2.10, 7, true);
                    """);
            statement.execute("""
                    INSERT INTO discount_card (id, number, amount) VALUES
                    (1, 1111, 3);
                    """);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    DROP TABLE IF EXISTS product, discount_card;
                    """);
        }

        Files.deleteIfExists(Paths.get(TEST_CSV_FILE_PATH));
    }

    @Test
    @DisplayName("Test main method with valid data")
    public void testMain() throws IOException {
        String[] args = {
                "2-1", "3-1", "2-5",
                "discountCard=1111", "balanceDebitCard=100",
                "saveToFile=" + TEST_CSV_FILE_PATH,
                "datasource.url=" + DB_URL,
                "datasource.username=" + DB_USERNAME,
                "datasource.password=" + DB_PASSWORD
        };
        CheckRunner.main(args);

        File file = new File(TEST_CSV_FILE_PATH);
        assertTrue(file.exists());

        List<String> lines = Files.readAllLines(Path.of(TEST_CSV_FILE_PATH));
        if (lines.size() > 1) {
            lines.remove(1);
        }
        String fileContent = String.join("\n", lines);
        assertEquals(EXPECTED_CSV_CONTENT, fileContent);
    }
}
