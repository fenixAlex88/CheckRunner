package ru.clevertec.check.services;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;

import java.util.NoSuchElementException;

public class ProductServiceDBImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceDBImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт с ID " + id + " не найден"));
    }
}
