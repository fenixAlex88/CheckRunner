package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;

import java.io.*;
import java.util.List;

public class CSVWorkerImpl implements CSVWorker {
    private final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);

    @Override
    public void writeToCSV(String filePath, List<String[]> data, String delimiter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] lineData : data) {
                bw.write(String.join(delimiter, lineData));
                bw.newLine();
            }
        } catch (IOException e) {
            throw internalServerErrorException;
        }
    }

    @Override
    public void writeErrorToCSV(String message, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("ERROR");
            bw.newLine();
            bw.write(message);
        } catch (IOException e) {
            throw internalServerErrorException;
        }
    }
}
