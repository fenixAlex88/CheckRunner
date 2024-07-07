package ru.clevertec.check.services;

import ru.clevertec.check.utils.CSVWorker;

import java.util.List;

public class DiscountService {
    private static final String DISCOUNT_CARDS_CSV = "./src/main/resources/discountCards.csv";

    public static int getDiscountByCardNumber(String cardNumber) {
        List<String[]> discountCardsData = CSVWorker.readCSV(DISCOUNT_CARDS_CSV, ";");
        for (String[] cardData : discountCardsData) {
            if (cardData[1].equals(cardNumber)) {
                return Integer.parseInt(cardData[2]);
            }
        }
        return 0;
    }
}
