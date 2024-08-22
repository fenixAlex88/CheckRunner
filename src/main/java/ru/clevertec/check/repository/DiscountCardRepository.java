package ru.clevertec.check.repository;

import ru.clevertec.check.model.DiscountCard;

import java.util.Optional;

public interface DiscountCardRepository {
    Optional<DiscountCard> getDiscountCardByNumber(int number);
}
