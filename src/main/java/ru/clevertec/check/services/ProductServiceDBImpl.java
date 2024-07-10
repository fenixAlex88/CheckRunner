package ru.clevertec.check.services;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;

public class ProductServiceDBImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceDBImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }
}
