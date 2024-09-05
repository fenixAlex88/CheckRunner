package ru.clevertec.check.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountCardServiceDBImpl Tests")
public class DiscountCardServiceDBImplTest {

    @Mock
    private DiscountCardRepository discountCardRepository;

    @InjectMocks
    private DiscountCardServiceDBImpl discountCardService;

    @Test
    @DisplayName("Test getDiscountCardByNumber returns discount card when found")
    public void testGetDiscountCardByNumberSuccess() {
        DiscountCard discountCard = new DiscountCard.Builder()
                .setId(1)
                .setNumber(1111)
                .setAmount(5)
                .build();

        when(discountCardRepository.getDiscountCardByNumber(1111)).thenReturn(Optional.of(discountCard));

        DiscountCard result = discountCardService.getDiscountCardByNumber(1111);

        assertEquals(discountCard, result);
    }

    @Test
    @DisplayName("Test getDiscountCardByNumber throws exception when not found")
    public void testGetDiscountCardByNumberNotFound() {
        when(discountCardRepository.getDiscountCardByNumber(1111)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> discountCardService.getDiscountCardByNumber(1111));
    }
}