package edu.school21.chat.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnect {
    public static HikariDataSource connectDb() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        hikariConfig.setValidationTimeout(300_000);

        return new HikariDataSource(hikariConfig);

    }
}
