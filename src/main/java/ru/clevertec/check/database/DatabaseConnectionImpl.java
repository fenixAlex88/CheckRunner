package ru.clevertec.check.database;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.utils.ArgsParser;
import ru.clevertec.check.utils.ArgsParserImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseConnectionImpl implements DatabaseConnection {
    private static DatabaseConnectionImpl instance;
    private Connection connection;

    private DatabaseConnectionImpl() {
    }

    public static synchronized DatabaseConnectionImpl getInstance() {
        return Optional.ofNullable(instance)
                .orElseGet(() -> {
                    instance = new DatabaseConnectionImpl();
                    return instance;
                });
    }


    public Connection getConnection() {
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
        try {
            connection = Optional.ofNullable(connection)
                    .filter(conn -> {
                        try {
                            return !conn.isClosed();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseGet(() -> {
                        try {
                            DriverManager.registerDriver(new org.postgresql.Driver());
                            return DriverManager.getConnection(
                                    argsParser.getDatasourceUrl(),
                                    argsParser.getDatasourceUsername(),
                                    argsParser.getDatasourcePassword());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (RuntimeException e) {
            throw CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);
        }
        return connection;
    }
}
