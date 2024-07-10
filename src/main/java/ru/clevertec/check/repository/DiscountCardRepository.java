package ru.clevertec.check.repository;

import ru.clevertec.check.model.DiscountCard;

public interface DiscountCardRepository {
    DiscountCard getDiscountCardByNumber(int number);
}
