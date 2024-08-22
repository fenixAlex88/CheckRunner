package ru.clevertec.check.repository;

import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {
    private final DatabaseConnection dbConnection;
    private static final String SQL_SELECT_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";

    public ProductRepositoryImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<Product> findById(int id) {
    final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL_SELECT_PRODUCT_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product.Builder()
                            .setId(resultSet.getInt("id"))
                            .setDescription(resultSet.getString("description"))
                            .setPrice(resultSet.getDouble("price"))
                            .setQuantityInStock(resultSet.getInt("quantity_in_stock"))
                            .setWholesale(resultSet.getBoolean("wholesale_product"))
                            .build();
                    return Optional.of(product);
                }
            }
        } catch (SQLException e) {
            throw internalServerErrorException;
        }
        return Optional.empty();
    }
}

