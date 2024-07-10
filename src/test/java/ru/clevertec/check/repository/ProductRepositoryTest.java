package ru.clevertec.check.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProductRepositoryTest {
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
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dbConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(resultSet);
    }

    @Test
    void findById_Should_Return_Product_When_Id_Is_Valid() throws SQLException {
        int id = 1;
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(id);
        when(resultSet.getString("description")).thenReturn("Test Product");
        when(resultSet.getDouble("price")).thenReturn(10.0);
        when(resultSet.getInt("quantity_in_stock")).thenReturn(100);
        when(resultSet.getBoolean("wholesale_product")).thenReturn(true);

        Product product = productRepository.findById(id);

        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals("Test Product", product.getDescription());
        assertEquals(10.0, product.getPrice());
        assertEquals(100, product.getQuantityInStock());
        assertTrue(product.isWholesale());
    }

    @Test
    void findById_Should_Return_Null_When_Id_Is_Not_Found() throws SQLException {
        int id = 1;
        when(resultSet.next()).thenReturn(false);

        Product product = productRepository.findById(id);

        assertNull(product);
    }

    @Test
    void findById_Should_Throw_Exception_When_SQLException_Occurs() throws SQLException {
        int id = 1;
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productRepository.findById(id));
    }
}
