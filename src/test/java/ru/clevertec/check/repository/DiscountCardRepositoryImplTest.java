package ru.clevertec.check.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.model.DiscountCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountCardRepositoryImpl Tests")
public class DiscountCardRepositoryImplTest {

    @Mock
    private DatabaseConnection dbConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement pstmt;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private DiscountCardRepositoryImpl discountCardRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        when(dbConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(pstmt);
    }

    @Test
    @DisplayName("Test getDiscountCardByNumber returns discount card when found")
    public void testGetDiscountCardByNumber_Found() throws SQLException {
        int cardNumber = 123;
        when(pstmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("number")).thenReturn(cardNumber);
        when(resultSet.getInt("amount")).thenReturn(100);

        Optional<DiscountCard> result = discountCardRepository.getDiscountCardByNumber(cardNumber);

        assertTrue(result.isPresent());
        assertEquals(cardNumber, result.get().getNumber());
        assertEquals(100, result.get().getAmount());
    }

    @Test
    @DisplayName("Test getDiscountCardByNumber returns empty when discount card not found")
    public void testGetDiscountCardByNumber_NotFound() throws SQLException {
        int cardNumber = 123;
        when(pstmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<DiscountCard> result = discountCardRepository.getDiscountCardByNumber(cardNumber);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test getDiscountCardByNumber throws RuntimeException on SQLException")
    public void testGetDiscountCardByNumber_SQLException() throws SQLException {
        int cardNumber = 123;
        when(pstmt.executeQuery()).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> discountCardRepository.getDiscountCardByNumber(cardNumber));
    }
}