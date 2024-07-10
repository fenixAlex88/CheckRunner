package ru.clevertec.check.utils;

import java.util.List;

public interface ArgsParser {

    void parse(String[] args);

    int getDiscountCard();

    double getBalanceDebitCard();

    List<String[]> getProductsList();
}