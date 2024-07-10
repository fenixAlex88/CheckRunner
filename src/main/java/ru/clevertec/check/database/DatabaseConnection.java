package ru.clevertec.check.database;

import java.sql.Connection;

public interface DatabaseConnection {
    Connection getConnection();
}
