/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionBatchResult;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchFailureTest {
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
    void atomicBatchCommitsNothingWhenACallFails() {
        var config = configuration(true);

        var result = insertBirthdays(config, 10, 10, 11);

        assertTrue(result.hasExceptions());
        assertEquals(0, result.rows());
        assertEquals(0, countBirthdays(config, 10, 11));
    }

    @Test
    void nonAtomicBatchKeepsTheOtherCalls() {
        var config = configuration(false);

        var result = insertBirthdays(config, 20, 20, 21);

        assertTrue(result.hasExceptions());
        assertEquals(2, result.rows());
        assertEquals(2, countBirthdays(config, 20, 21));
    }

    private static QueryConfiguration configuration(boolean atomic) {
        return new QueryConfigurationBuilder(db.dataSource())
                .setAtomic(atomic)
                .setExceptionHandler(e -> {})
                .build();
    }

    private static InsertionBatchResult<InsertionResult> insertBirthdays(QueryConfiguration config, int... ids) {
        var calls = Arrays.stream(ids)
                .mapToObj(id -> Call.of().bind(id).bind("2000-01-01"))
                .toList();
        return config.query("INSERT INTO birthdays(user_id, birth_date) VALUES (?, ?::date)")
                .batch(calls)
                .insert();
    }

    private static long countBirthdays(QueryConfiguration config, int first, int second) {
        return config.query("SELECT count(*) AS count FROM birthdays WHERE user_id IN (?, ?)")
                .single(Call.of().bind(first).bind(second))
                .map(row -> row.getLong("count"))
                .first()
                .orElseThrow();
    }
}
