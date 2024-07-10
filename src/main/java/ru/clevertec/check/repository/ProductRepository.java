package ru.clevertec.check.repository;

import ru.clevertec.check.model.Product;

public interface ProductRepository {
    Product findById(int id);

}
