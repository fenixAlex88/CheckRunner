package ru.clevertec.check.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVWorkerTest {
    @TempDir
    Path tempDir;

    @Test
    public void testWriteToCSV() throws IOException {
        CSVWorker csvWorker = new CSVWorkerImpl();
        Path filePath = tempDir.resolve("test.csv");
        List<String[]> data = Arrays.asList(
                new String[] {"ID", "Name", "Price"},
                new String[] {"1", "Apple", "0.99"},
                new String[] {"2", "Banana", "0.59"}
        );
        String delimiter = ",";

        assertDoesNotThrow(() -> csvWorker.writeToCSV(filePath.toString(), data, delimiter));

        List<String> lines = Files.readAllLines(filePath);
        assertEquals("ID,Name,Price", lines.get(0));
        assertEquals("1,Apple,0.99", lines.get(1));
        assertEquals("2,Banana,0.59", lines.get(2));
    }

    @Test
    public void testWriteErrorToCSV() throws IOException {
        CSVWorker csvWorker = new CSVWorkerImpl();
        Path filePath = tempDir.resolve("error.csv");
        String errorMessage = "An error occurred";

        assertDoesNotThrow(() -> csvWorker.writeErrorToCSV(errorMessage, filePath.toString()));

        List<String> lines = Files.readAllLines(filePath);
        assertEquals("ERROR", lines.get(0));
        assertEquals(errorMessage, lines.get(1));
    }

    @Test
    public void testWriteToCSVWithIOException() {
        CSVWorker csvWorker = new CSVWorkerImpl();
        String invalidFilePath = "/invalid/path/test.csv";
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"ID", "Name", "Price"});
        String delimiter = ",";

        assertThrows(RuntimeException.class, () -> csvWorker.writeToCSV(invalidFilePath, data, delimiter));
    }

    @Test
    public void testWriteErrorToCSVWithIOException() {
        CSVWorker csvWorker = new CSVWorkerImpl();
        String invalidFilePath = "/invalid/path/error.csv";
        String errorMessage = "An error occurred";

        assertThrows(RuntimeException.class, () -> csvWorker.writeErrorToCSV(errorMessage, invalidFilePath));
    }
}
