package ru.clevertec.check.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceDBImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductById_Should_Return_Product_When_Id_Is_Valid() {
        int id = 1;
        Product expectedProduct = new Product.Builder()
                .setId(id)
                .setDescription("Product Description")
                .setPrice(9.99)
                .setQuantityInStock(10)
                .setWholesale(false)
                .build();
        when(productRepository.findById(id)).thenReturn(expectedProduct);

        Product actualProduct = productService.getProductById(id);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void getProductById_Should_Return_Null_When_Id_Is_Invalid() {
        int id = -1;
        when(productRepository.findById(id)).thenReturn(null);

        Product actualProduct = productService.getProductById(id);

        assertNull(actualProduct);
    }
}
