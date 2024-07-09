package ru.clevertec.check.services;

public interface CheckService {
    void parseArgs(String[] args);

    void saveCheckToCSV(String filePath);

    void printCheckToConsole();
}
