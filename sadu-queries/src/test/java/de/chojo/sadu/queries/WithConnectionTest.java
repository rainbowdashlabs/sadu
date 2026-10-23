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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WithConnectionTest {
    private static PostgresDatabase.Database db;
    private final List<SQLException> handled = new ArrayList<>();
    private QueryConfiguration config;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
    }

    @AfterAll
    static void afterAll() {
        db.close();
    }

    @BeforeEach
    void setUp() {
        config = new QueryConfigurationBuilder(db.dataSource())
                .setExceptionHandler(handled::add)
                .build();
    }

    @Test
    void commitsAnAutoCommitConnection() throws Exception {
        var connection = db.dataSource().getConnection();
        connection.setAutoCommit(true);

        try (var conn = config.withConnection(connection)) {
            insertUser(conn, "injected");
        }

        assertTrue(handled.isEmpty());
        assertEquals(1, countUsers("injected"));
    }

    @Test
    void rollsBackAnAutoCommitConnectionOnError() throws Exception {
        var connection = db.dataSource().getConnection();
        connection.setAutoCommit(true);

        try (var conn = config.withConnection(connection)) {
            insertUser(conn, "rolled back");
            conn.query("INSERT INTO missing_table(id) VALUES (1)").single().insert();
        }

        assertEquals(0, countUsers("rolled back"));
    }

    private static void insertUser(QueryConfiguration conn, String name) {
        conn.query("INSERT INTO users(uuid, name) VALUES (gen_random_uuid(), ?)")
                .single(Call.of().bind(name))
                .insert();
    }

    private long countUsers(String name) {
        return config.query("SELECT count(*) AS count FROM users WHERE name = ?")
                .single(Call.of().bind(name))
                .map(row -> row.getLong("count"))
                .first()
                .orElseThrow();
    }
}
