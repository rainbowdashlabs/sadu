/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.examples.dao.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.api.call.Call.call;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "OptionalGetWithoutIsPresent", "RedundantExplicitVariableType"})
public class BindTest {

    private QueryConfiguration query;
    private PostgresDatabase.Database db;


    @BeforeEach
    void before() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())
                                                                                                           .register(RowMapper.forClass(User.class).mapper(User.map()).build())).build();
    }

    @AfterEach
    void after() {
        db.close();
    }

    // Retrieve all matching users directly
    @Test
    public void failOnInvalidToken() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            List<User> users = query.query("SELECT * FROM users WHERE id = ? AND name ILIKE :name1")
                                    .single(call().bind(1).bind("name1", "lilly"))
                                    .map(User.map())
                                    .all();
        });
    }
}
