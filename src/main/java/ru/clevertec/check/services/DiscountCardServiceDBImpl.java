package ru.clevertec.check.services;

import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;

import java.util.NoSuchElementException;

public class DiscountCardServiceDBImpl implements DiscountCardService {
    private final DiscountCardRepository discountCardRepository;

    public DiscountCardServiceDBImpl(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(int number) {
        return discountCardRepository.getDiscountCardByNumber(number)
                .orElseThrow(() -> new NoSuchElementException("Дисконтная карта с номером" + number + " не найдена"));
    }
}
