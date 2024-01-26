/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.databases.PostgreSql;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mapper.PostgresqlMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static de.chojo.sadu.queries.PostgresDatabase.createContainer;

class QueryExample {
    private static GenericContainer<?> pg;
    private static QueryConfiguration query;

    @BeforeAll
    static void beforeAll() throws IOException {
        pg = createContainer("postgres", "postgres");
        DataSource dc = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(pg.getHost()).port(pg.getFirstMappedPort())).create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();

        List<RowMapper<?>> defaultMapper = PostgresqlMapper.getDefaultMapper();
        RowMapperRegistry registry = new RowMapperRegistry().register(defaultMapper);
        query = new QueryConfigurationBuilder(dc).setRowMapperRegistry(registry).build();
    }

    @AfterAll
    static void afterAll() throws IOException {
        pg.close();
    }

    @Test
    public void example() {
        // A query with a named parameter
        query.query("SELECT * FROM table where uuid = :uuid");

        // A query with a named parameter and one indexed parameter
        query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name");

        // A query with an indexed parameter
        query.query("INSERT INTO table VALUES(?)");
    }


}
