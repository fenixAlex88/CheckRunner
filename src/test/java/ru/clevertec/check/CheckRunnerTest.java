package ru.clevertec.check;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.utils.ArgsParser;
import ru.clevertec.check.utils.CSVWorker;

import static org.mockito.Mockito.*;


@Disabled
class CheckRunnerTest {

    private ArgsParser argsParser;
    private CheckService checkService;
    private CSVWorker csvWorker;

    @BeforeEach
    void setUp() {
        argsParser = mock(ArgsParser.class);
        checkService = mock(CheckService.class);
        csvWorker = mock(CSVWorker.class);
    }

    @Test
    void testMainSuccess() {
        String[] args = {"2-1", "3-1", "2-5", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv", "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.username=postgres", "datasource.password=postgres"};
        doNothing().when(argsParser).parse(args);
        when(argsParser.getSaveToFilePath()).thenReturn(CheckRunner.DEFAULT_RESULT_CSV);

        CheckRunner.main(args);

        verify(checkService).printCheckToConsole();
        verify(checkService).saveCheckToCSV(CheckRunner.DEFAULT_RESULT_CSV);
    }

    @Test
    void testMainException() {
        String[] args = {"2-1", "3-1", "2-5", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv", "datasource.url=jdbc:postgresql://localhost:5432/check", "datasource.username=postgres", "datasource.password=postgres"};
        doThrow(new RuntimeException("Parsing error")).when(argsParser).parse(args);
        when(argsParser.getSaveToFilePath()).thenReturn(CheckRunner.DEFAULT_RESULT_CSV);

        CheckRunner.main(args);

        verify(csvWorker).writeErrorToCSV("Parsing error", CheckRunner.DEFAULT_RESULT_CSV);
    }
}
