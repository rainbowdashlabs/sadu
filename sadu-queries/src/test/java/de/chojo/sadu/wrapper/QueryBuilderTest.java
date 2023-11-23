/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.util.List;

import static de.chojo.sadu.MariaDbDatabase.createContainer;

class QueryBuilderTest {
    private static GenericContainer<?> db;
    private HikariDataSource source;
    @Language("mariadb")
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS test(
                id   INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_name TEXT   NOT NULL
                )
                """;
    @Language("mariadb")
    private static final String INSERT_SINGLE = "INSERT INTO test(user_name) VALUES(?)";
    @Language("mariadb")
    private static final String INSERT_MULTI = "INSERT INTO test(user_name) VALUES(?),(?),(?)";

    @BeforeAll
    public static void setup() {
        db = createContainer("root", "root");
        QueryBuilderConfig.setDefault(QueryBuilderConfig.builder().throwExceptions().build());
    }

    @BeforeEach
    public void setupDB() {
        source = DataSourceCreator.create(MariaDb.get())
                .configure(config -> config.host(db.getHost()).port(db.getFirstMappedPort()).database("mysql"))
                .create()
                .usingUsername("root")
                .usingPassword("root")
                .build();
    }

    @AfterEach
    public void after() throws IOException {
        source.close();
    }

    @Test
    public void getKey() {
        QueryBuilder.builder(source).defaultConfig()
                .queryWithoutParams(CREATE_TABLE)
                .update()
                .send()
                .join();

        long l = QueryBuilder.builder(source).defaultConfig()
                .query(INSERT_SINGLE)
                .parameter(stmt -> stmt.setString("test"))
                .insert()
                .keySync()
                .orElse(-1L);

        Assertions.assertNotEquals(-1, l);
    }

    @Test
    public void getKeys() {
        QueryBuilder.builder(source).defaultConfig()
                .queryWithoutParams(CREATE_TABLE)
                .update()
                .send()
                .join();

        List<Long> l = QueryBuilder.builder(source).defaultConfig()
                .query(INSERT_MULTI)
                .parameter(stmt -> stmt.setString("test").setString("test").setString("test"))
                .insert()
                .keysSync();

        // Only the last key is returned. Multiple keys would be returned if there would be multiple keys in a single row.
        Assertions.assertEquals(1, l.size());
    }

}
