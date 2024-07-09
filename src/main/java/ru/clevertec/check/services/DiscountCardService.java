package ru.clevertec.check.services;

import ru.clevertec.check.model.DiscountCard;

public interface DiscountCardService {
    DiscountCard getDiscountCardByNumber(int number);
}
