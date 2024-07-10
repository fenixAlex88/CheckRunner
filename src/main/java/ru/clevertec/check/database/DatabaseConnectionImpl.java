package ru.clevertec.check.database;

import ru.clevertec.check.exception.CustomExceptionFactory;
import ru.clevertec.check.exception.CustomExceptionType;
import ru.clevertec.check.utils.ArgsParser;
import ru.clevertec.check.utils.ArgsParserImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionImpl implements DatabaseConnection {
    private static DatabaseConnectionImpl instance;
    private Connection connection;

    private DatabaseConnectionImpl() {
    }

    public static synchronized DatabaseConnectionImpl getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionImpl();
        }
        return instance;
    }

    public Connection getConnection() {
        final RuntimeException internalServerErrorException = CustomExceptionFactory.createException(CustomExceptionType.INTERNAL_SERVER_ERROR);
        ArgsParser argsParser = ArgsParserImpl.INSTANCE;
        try {
            if (this.connection == null || this.connection.isClosed()) {
                DriverManager.registerDriver(new org.postgresql.Driver());
                connection = DriverManager.getConnection(
                        argsParser.getDatasourceUrl(),
                        argsParser.getDatasourceUsername(),
                        argsParser.getDatasourcePassword()
                );
            }
        } catch (SQLException e) {
            throw internalServerErrorException;
        }
        return connection;
    }
}
