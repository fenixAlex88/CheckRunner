package ru.clevertec.check.repository;

import ru.clevertec.check.model.Product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(int id);
}
