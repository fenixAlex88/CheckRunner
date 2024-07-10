package ru.clevertec.check.services;

public interface CheckService {
    void generateCheck();

    void saveCheckToCSV(String filePath);

    void printCheckToConsole();
}
