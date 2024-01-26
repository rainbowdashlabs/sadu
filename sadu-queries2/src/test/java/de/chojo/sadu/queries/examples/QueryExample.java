/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.PostgresqlMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static de.chojo.sadu.PostgresDatabase.createContainer;

class QueryExample {
    private static QueryConfiguration query;
    private static PostgresDatabase.Database db;

    @BeforeAll
    static void beforeAll() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())).build();
    }

    @AfterAll
    static void afterAll() throws IOException {
        db.close();
    }

    @Test
    @Disabled
    public void example() {
        // A query with a named parameter
        query.query("SELECT * FROM table where uuid = :uuid");

        // A query with a named parameter and one indexed parameter
        query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name");

        // A query with an indexed parameter
        query.query("INSERT INTO table VALUES(?)");
    }


}
