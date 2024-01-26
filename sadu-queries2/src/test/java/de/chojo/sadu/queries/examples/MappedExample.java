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
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.stages.results.reading.MultiResult;
import de.chojo.sadu.queries.stages.results.reading.SingleResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.chojo.sadu.queries.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_BYTES;
import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_STRING;

public class MappedExample {

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
        // Retrieve all matching users directly
        List<User> users = query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .single(Calls.single(c -> c.bind("some input").bind("name", "user")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .allAndGet();

        // Retrieve the first user object directly
        Optional<User> user = query.query("SELECT * FROM table where uuid = :uuid")
                .single(Calls.single(c -> c.bind("uuid", UUID.randomUUID(), AS_BYTES)))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .oneAndGet();

        // Retrieve all matching users and store them to use them again later
        // From here on another query could be issued that uses the results of this query
        MultiResult<List<User>> usersResult = query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .single(Calls.single(c -> c.bind("some input").bind("name", "user")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .all();

        // Retrieve the first user and store them it to use it again later
        // From here on another query could be issued that uses the results of this query
        SingleResult<User> userResult2 = query.query("INSERT INTO users(uuid, name) VALUES (:uuid, :name) RETURNING id, uuid, name")
                .single(Calls.single(c -> c.bind("uuid", UUID.randomUUID(), AS_STRING).bind("name", "lilly")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .storeOneAndAppend("user")
                .query("INSERT INTO birthdays(user_id, birth_date) VALUES (:id, :date)")
                .single(storage -> Calls.single(r -> r.bind("id", storage.getAs("user", User.class).id).bind("date", LocalDate.of(1990, 1,1))))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .one();
    }

    public class User {
        private final int id;
        private final UUID uuid;
        private final String name;

        public User(int id, UUID uuid, String name) {
            this.id = id;
            this.uuid = uuid;
            this.name = name;
        }
    }

}
