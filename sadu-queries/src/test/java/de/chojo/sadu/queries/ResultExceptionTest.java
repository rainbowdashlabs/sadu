/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultExceptionTest {
    private static PostgresDatabase.Database db;
    private static QueryConfiguration query;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource())
                .setExceptionHandler(e -> {})
                .build();
    }

    @AfterAll
    static void afterAll() {
        db.close();
    }

    @Test
    void failedUpdateHasExceptions() {
        var result = query.query("INSERT INTO missing_table(id) VALUES (?)")
                .single(Call.of().bind(1))
                .insert();

        assertTrue(result.hasExceptions());
        assertFalse(result.isSuccess());
    }

    @Test
    void failedReadHasExceptions() {
        var result = query.query("SELECT id FROM missing_table")
                .single()
                .map(row -> row.getInt("id"))
                .allResults();

        assertTrue(result.hasExceptions());
    }

    @Test
    void successfulQueryHasNoExceptions() {
        var result = query.query("SELECT 1 AS id")
                .single()
                .map(row -> row.getInt("id"))
                .firstResult();

        assertFalse(result.hasExceptions());
    }
}
