package ru.clevertec.check.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DiscountCardServiceTest {

    @Mock
    private DiscountCardRepository discountCardRepository;

    @InjectMocks
    private DiscountCardServiceDBImpl discountCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDiscountCardByNumber_Should_Return_DiscountCard_When_Number_Is_Valid() {
        int number = 123456;
        DiscountCard expectedDiscountCard = new DiscountCard.Builder()
                .setNumber(number)
                .setId(10)
                .setAmount(10)
                .build();
        when(discountCardRepository. getDiscountCardByNumber(number)).thenReturn(expectedDiscountCard);

        DiscountCard actualDiscountCard = discountCardService.getDiscountCardByNumber(number);

        assertNotNull(actualDiscountCard);
        assertEquals(expectedDiscountCard, actualDiscountCard);
    }

    @Test
    void getDiscountCardByNumber_Should_Return_Null_When_Number_Is_Invalid() {
        int number = -1;
        when(discountCardRepository.getDiscountCardByNumber(number)).thenReturn(null);

        DiscountCard actualDiscountCard = discountCardService.getDiscountCardByNumber(number);

        assertNull(actualDiscountCard);
    }
}
