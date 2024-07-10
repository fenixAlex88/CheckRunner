package ru.clevertec.check.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.model.DiscountCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DiscountCardRepositoryTest {

    private static final String SQL_SELECT_DISCOUNT_CARD_BY_NUMBER ="SELECT * FROM discount_card WHERE number = ?";
    @Mock
    private DatabaseConnection dbConnection;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private DiscountCardRepositoryImpl discountCardRepository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dbConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(SQL_SELECT_DISCOUNT_CARD_BY_NUMBER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    void getDiscountCardByNumber_Should_Return_DiscountCard_When_Number_Is_Valid() throws SQLException {
        int number = 123456;
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("number")).thenReturn(number);
        when(resultSet.getInt("amount")).thenReturn(10);

        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(number);

        assertNotNull(discountCard);
        assertEquals(1, discountCard. getId());
        assertEquals(number, discountCard.getNumber());
        assertEquals(10, discountCard.getAmount());
    }

    @Test
    void getDiscountCardByNumber_Should_Return_Null_When_Number_Is_Not_Found() throws SQLException {
        int number = 123456;
        when(resultSet.next()).thenReturn(false);

        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(number);

        assertNull(discountCard);
    }

    @Test
    void getDiscountCardByNumber_Should_Throw_Exception_When_SQLException_Occurs() throws SQLException {
        int number = 123456;
        when(connection.prepareStatement(SQL_SELECT_DISCOUNT_CARD_BY_NUMBER)).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> discountCardRepository.getDiscountCardByNumber(number));
    }
}
