package ru.clevertec.check.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CSVWorkerImpl Tests")
class CSVWorkerImplTest {

    private final CSVWorker csvWorker = new CSVWorkerImpl();
    private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

    @Test
    @DisplayName("Test writing data to CSV")
    void testWriteToCSV(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("data.csv");
        List<String[]> data = List.of(
                new String[]{"id", "name", "price"},
                new String[]{"1", "Apple", "0.99"},
                new String[]{"2", "Banana", "0.59"}
        );
        String delimiter = ";";

        csvWorker.writeToCSV(filePath.toString(), data, delimiter);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            assertEquals("id;name;price", br.readLine());
            assertEquals("1;Apple;0.99", br.readLine());
            assertEquals("2;Banana;0.59", br.readLine());
        }
    }

    @Test
    @DisplayName("Test writing error to CSV")
    void testWriteErrorToCSV(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve("error.csv");
        String errorMessage = "Error message";

        csvWorker.writeErrorToCSV(errorMessage, filePath.toString());

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            assertEquals("ERROR", br.readLine());
            assertEquals(errorMessage, br.readLine());
        }
    }

    @Test
    @DisplayName("Test writeToCSV throws exception on IO error")
    void testWriteToCSVThrowsException() {
        List<String[]> data = List.of(
                new String[]{"id", "name", "price"},
                new String[]{"1", "Apple", "0.99"},
                new String[]{"2", "Banana", "0.59"}
        );
        String delimiter = ";";

        assertThrows(internalServerErrorException.getClass(), () -> {
            csvWorker.writeToCSV("invalid/path", data, delimiter);
        });
    }

    @Test
    @DisplayName("Test writeErrorToCSV throws exception on IO error")
    void testWriteErrorToCSVThrowsException() {
        String errorMessage = "Error message";

        assertThrows(internalServerErrorException.getClass(), () -> {
            csvWorker.writeErrorToCSV(errorMessage, "invalid/path");
        });
    }
}
