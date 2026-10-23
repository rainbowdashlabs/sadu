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
import static org.junit.jupiter.api.Assertions.assertEquals;

class AtomicTest {
    private static PostgresDatabase.Database db;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
    }

    @AfterAll
    static void afterAll() {
        db.close();
    }

    @Test
    void atomicTransactionRollsBackOnError() {
        var config = configuration(true);
        try (var conn = config.withSingleTransaction()) {
            insertUser(conn, "atomic");
            failingInsert(conn);
        }

        assertEquals(0, countUsers(config, "atomic"));
    }

    @Test
    void nonAtomicTransactionKeepsEachStatement() {
        var config = configuration(false);
        try (var conn = config.withSingleTransaction()) {
            insertUser(conn, "non-atomic");
            failingInsert(conn);
        }

        assertEquals(1, countUsers(config, "non-atomic"));
    }

    private static QueryConfiguration configuration(boolean atomic) {
        return new QueryConfigurationBuilder(db.dataSource())
                .setAtomic(atomic)
                .setExceptionHandler(e -> {})
                .build();
    }

    private static void insertUser(QueryConfiguration conn, String name) {
        conn.query("INSERT INTO users(uuid, name) VALUES (gen_random_uuid(), ?)")
                .single(Call.of().bind(name))
                .insert();
    }

    private static void failingInsert(QueryConfiguration conn) {
        conn.query("INSERT INTO missing_table(id) VALUES (1)").single().insert();
    }

    private static long countUsers(QueryConfiguration config, String name) {
        return config.query("SELECT count(*) AS count FROM users WHERE name = ?")
                .single(Call.of().bind(name))
                .map(row -> row.getLong("count"))
                .first()
                .orElseThrow();
    }
}
