/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.call.calls.Calls;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.examples.dao.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.api.call.Call.call;
import static de.chojo.sadu.queries.call.adapter.UUIDAdapter.AS_STRING;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "OptionalGetWithoutIsPresent", "RedundantExplicitVariableType"})
public class TransactionTest {
    private static QueryConfiguration query;
    private static PostgresDatabase.Database db;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())).build();
    }

    @AfterAll
    static void afterAll() {
        db.close();
    }

    @Test
    public void example() {
        // atomic transaction
        try (var conn = query.withSingleTransaction()) {
            // Retrieve the first user and store them it to use it again later
            // From here on another query could be issued that uses the results of this query
            ManipulationResult manipulation = conn.query("INSERT INTO users(uuid, name) VALUES (:uuid::uuid, :name) RETURNING id, uuid, name")
                    .single(call().bind("uuid", UUID.randomUUID(), AS_STRING).bind("name", "lilly"))
                    .map(User.map())
                    .storeOneAndAppend("user")
                    .query("INSERT INTO birthdays(user_id, birth_date) VALUES (:id, :date)")
                    // produce error
                    .single(storage -> call().bind("id", storage.getAs("user", User.class).get().id()).bind("date", "").asSingleCall())
                    .insert();
        }

        List<User> users = query.query("SELECT * FROM users")
                .single(Calls.empty())
                .map(User.map())
                .all();

        // Make sure that the first insert was not commited
        Assertions.assertEquals(0, users.size());
    }


}
