package ru.clevertec.check.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductRepositoryImpl Tests")
public class ProductRepositoryImplTest {

    @Mock
    private DatabaseConnection dbConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement pstmt;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        when(dbConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(pstmt);
    }

    @Test
    @DisplayName("Test findById returns product when found")
    public void testFindProductById_Found() throws SQLException {
        int id = 1;
        when(pstmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("description")).thenReturn("Test product");
        when(resultSet.getDouble("price")).thenReturn(100.00);
        when(resultSet.getInt("quantity_in_stock")).thenReturn(10);
        when(resultSet.getBoolean("wholesale_product")).thenReturn(true);

        Optional<Product> result = productRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Test product", result.get().getDescription());
        assertEquals(100.00, result.get().getPrice());
        assertTrue(result.get().isWholesale());
        assertEquals(10, result.get().getQuantityInStock());
    }

    @Test
    @DisplayName("Test findById returns empty when product not found")
    public void testFindProductById_NotFound() throws SQLException {
        int id = 1;
        when(pstmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Product> result = productRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test findById throws RuntimeException on SQLException")
    public void testFindProductById_SQLException() throws SQLException {
        int id = 1;
        when(pstmt.executeQuery()).thenThrow(new SQLException());

        assertThrows(RuntimeException.class, () -> productRepository.findById(id));
    }
}