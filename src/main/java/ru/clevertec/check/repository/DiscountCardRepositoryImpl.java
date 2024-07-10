package ru.clevertec.check.repository;

import ru.clevertec.check.database.DatabaseConnection;
import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.model.DiscountCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountCardRepositoryImpl implements DiscountCardRepository {
    private final DatabaseConnection dbConnection;
    private static final String SQL_SELECT_DISCOUNT_CARD_BY_NUMBER = "SELECT * FROM discount_card WHERE number = ?";

    public DiscountCardRepositoryImpl(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public DiscountCard getDiscountCardByNumber(int number) {
        final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL_SELECT_DISCOUNT_CARD_BY_NUMBER)) {
            pstmt.setInt(1, number);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return new DiscountCard.Builder()
                            .setId(resultSet.getInt("id"))
                            .setNumber(resultSet.getInt("number"))
                            .setAmount(resultSet.getInt("amount"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw internalServerErrorException;
        }
        return null;
    }
}
