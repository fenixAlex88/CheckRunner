package ru.clevertec.check.services;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.utils.CSVWorker;
import ru.clevertec.check.utils.CSVWorkerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountCardServiceImpl implements DiscountCardService {

    private final Map<Integer, DiscountCard> discountCards = new HashMap<>();

    public DiscountCardServiceImpl(String filePath) {
        loadDiscountCards(filePath);
    }

    private void loadDiscountCards(String filePath) {
        CSVWorker csvWorker = new CSVWorkerImpl();
        List<String[]> discountCardsStrings = csvWorker.readFromCSV(filePath, ";");
        for (String[] discountCardString : discountCardsStrings) {
            int id = Integer.parseInt(discountCardString[0]);
            int number = Integer.parseInt(discountCardString[1]);
            int amount = Integer.parseInt(discountCardString[2]);
            discountCards.put(number, new DiscountCard.Builder()
                    .setId(id)
                    .setNumber(number)
                    .setAmount(amount)
                    .build());
        }
    }

    @Override
    public DiscountCard getDiscountCardByNumber(int number) {
        if (discountCards.containsKey(number)) {
            return discountCards.get(number);
        }
        return new DiscountCard.Builder()
                .setNumber(number)
                .setAmount(2).
                build();
    }
}
