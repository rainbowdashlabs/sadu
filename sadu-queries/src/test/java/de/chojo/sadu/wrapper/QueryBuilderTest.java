/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.databases.SqlType;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class QueryBuilderTest {
    private HikariDataSource source;

    @BeforeAll
    public static void setup() {
        QueryBuilderConfig.setDefault(QueryBuilderConfig.builder().throwExceptions().build());
    }

    @BeforeEach
    public void setupDB() {
        source = DataSourceCreator.create(SqLite.sqlite())
                .configure(config -> config.path("data.db"))
                .create()
                .build();
    }

    @AfterEach
    public void after() throws IOException {
        Files.delete(Path.of("data.db"));
        source.close();
    }

    @Test
    public void getKey() {
        QueryBuilder.builder(source).defaultConfig()
                .queryWithoutParams(
                        """
                                CREATE TABLE IF NOT EXISTS test(
                                    id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                    user_name TEXT   NOT NULL
                                    )
                                    """)
                .update()
                .execute()
                .join();

        long l = QueryBuilder.builder(source).defaultConfig()
                .query("INSERT INTO test(user_name) VALUES(?)")
                .parameter(stmt -> stmt.setString("test"))
                .insert()
                .keySync()
                .orElse(-1L);

        Assertions.assertNotEquals(-1, l);
    }

    @Test
    @Disabled
    public void getKeys() {
        QueryBuilder.builder(source).defaultConfig()
                .queryWithoutParams(
                        """
                                CREATE TABLE IF NOT EXISTS test(
                                    id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                    user_name TEXT   NOT NULL
                                    )
                                    """)
                .update()
                .execute()
                .join();

        List<Long> l = QueryBuilder.builder(source).defaultConfig()
                .query("INSERT INTO test(user_name) VALUES(?),(?),(?)")
                .parameter(stmt -> stmt.setString("test").setString("test").setString("test"))
                .insert()
                .keysSync();

        // For some reason sqlite returns only the last id instead of all three.
        Assertions.assertEquals(3, l.size());
    }

}
