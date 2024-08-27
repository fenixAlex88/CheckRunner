package ru.clevertec.check.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceDBImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceDBImpl productService;

    @Test
    public void testGetProductByIdSuccess() {
        Product product = new Product.Builder()
                .setId(1)
                .setDescription("Test Product")
                .setPrice(100.0)
                .setWholesale(true)
                .setQuantityInStock(50)
                .build();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1);

        assertEquals(product, result);
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> productService.getProductById(1));

        assertEquals("Продукт с ID 1 не найден", exception.getMessage());
    }
}
