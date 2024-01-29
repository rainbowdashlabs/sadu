/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.PostgresqlMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.results.reading.Result;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.examples.dao.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_STRING;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "OptionalGetWithoutIsPresent", "RedundantExplicitVariableType"})
public class ReadTest {

    private QueryConfiguration query;
    private PostgresDatabase.Database db;


    @BeforeEach
    void before() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())).build();
        query.query("INSERT INTO users(uuid, name) VALUES (?::uuid,?)")
                .batch(Call.of().bind(UUID.randomUUID(), AS_STRING).bind("Lilly"),
                        Call.of().bind(UUID.randomUUID(), AS_STRING).bind("Chojo"))
                .insert();
    }

    @AfterEach
    void after() {
        db.close();
    }

    // Retrieve all matching users directly
    @Test
    public void retrieveAllDirectly() {
        List<User> users = query.query("SELECT * FROM users WHERE id = ? AND name ILIKE :name")
                .single(Calls.single(call -> call.bind(1).bind("name", "lilly")))
                .map(User.map())
                .allAndGet();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    public void retrieveAllDirectlyNoFilter() {
        List<User> users = query.query("SELECT * FROM users")
                .single(Calls.empty())
                .map(User.map())
                .allAndGet();
        Assertions.assertEquals(2, users.size());
    }

    // Retrieve all matching users directly

    @Test
    public void retrieveFirstDirectly() {
        // Retrieve the first user object directly
        Optional<User> user = query.query("SELECT * FROM users where id = :id")
                .single(Calls.single(call -> call.bind("id", 1)))
                .map(User.map())
                .oneAndGet();
    }

    @Test
    public void retrieveAllMatching() {
        // Retrieve all matching users and store them to use them again later
        // From here on another query could be issued that uses the results of this query
        Result<List<User>> usersResult = query.query("SELECT * FROM users WHERE id = ? AND name ILIKE :name")
                .single(Calls.single(call -> call.bind(1).bind("name", "lilly")))
                .map(User.map())
                .storeOneAndAppend("user")
                .query("SELECT * FROM users WHERE uuid = :uuid::uuid")
                .single(storage -> Calls.single(Call.of().bind("uuid", storage.getAs("user", User.class).get().uuid(), AS_STRING)))
                .map(User.map())
                .all();
        Assertions.assertEquals(1, usersResult.result().size());
    }

    @Test
    public void storeAndUse() {
        // Retrieve the first user and store them it to use it again later
        // From here on another query could be issued that uses the results of this query
        ManipulationResult manipulation = query.query("INSERT INTO users(uuid, name) VALUES (:uuid::uuid, :name) RETURNING id, uuid, name")
                .single(Calls.single(call -> call.bind("uuid", UUID.randomUUID(), AS_STRING).bind("name", "lilly")))
                .map(User.map())
                .storeOneAndAppend("user")
                .query("INSERT INTO birthdays(user_id, birth_date) VALUES (:id, :date)")
                .single(storage -> Calls.single(r -> r.bind("id", storage.getAs("user", User.class).get().id()).bind("date", LocalDate.of(1990, 1, 1))))
                .insert();
        Assertions.assertEquals(1, manipulation.rows());
    }

}
