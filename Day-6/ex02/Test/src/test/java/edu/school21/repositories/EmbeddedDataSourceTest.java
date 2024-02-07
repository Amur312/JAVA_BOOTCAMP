package edu.school21.repositories;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddedDataSourceTest {
    private static final String BD_NAME = "EX01";
    private static final String BD_SCHEMA = "schema.sql";
    private static final String BD_DATA = "data.sql";
    private EmbeddedDatabase database;

    @BeforeEach
    public void init() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName(BD_NAME)
                .addScript(BD_SCHEMA)
                .addScript(BD_DATA)
                .build();
    }

    @Test
    public void testConnection() {
        try (Connection connection = database.getConnection()) {
            assertNotNull(connection);
        } catch (SQLException e) {
            assertNotNull(null);
        }
    }
}
