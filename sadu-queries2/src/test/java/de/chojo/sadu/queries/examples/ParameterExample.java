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
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.stages.execution.writing.CalledBatchQuery;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static de.chojo.sadu.queries.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_BYTES;

class ParameterExample {
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
        // Executing a single call by directly creating the call
        CalledSingletonQuery single1 = query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .single(Calls.single(Call.of().bind("some input").bind("name", "user")));

        // Creating a single call of the query by using the calls wrapper with a SingletonCall
        CalledSingletonQuery single2 = query.query("SELECT * FROM table where uuid = :uuid")
                .single(Calls.single(c -> c.bind("uuid", UUID.randomUUID(), AS_BYTES)));

        // Creating multiple calls of the same query by using the batch wrapper
        CalledBatchQuery batch = query.query("INSERT INTO table VALUES(?)")
                .batch(Calls.batch(
                        Call.of().bind(UUID.randomUUID(), AS_BYTES),
                        Call.of().bind(UUID.randomUUID(), AS_BYTES)));

        BatchCall collect = Stream.generate(UUID::randomUUID)
                .limit(10)
                .map(i -> Call.of().bind(i, AS_BYTES))
                .collect(Calls.collect());
    }


}
