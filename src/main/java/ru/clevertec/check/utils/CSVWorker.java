package ru.clevertec.check.utils;

import java.util.List;

public interface CSVWorker {
    List<String[]> readFromCSV(String filePath, String delimiter);
    void writeToCSV(String filePath, List<String[]> data, String delimiter);
    void writeErrorToCSV(String message, String filePath);
}