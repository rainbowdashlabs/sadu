/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.timeconversion;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.examples.dao.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.mapper.reader.StandardReader.INSTANT_FROM_MILLIS;
import static de.chojo.sadu.mapper.reader.StandardReader.INSTANT_FROM_SECONDS;
import static de.chojo.sadu.mapper.reader.StandardReader.INSTANT_FROM_TIMESTAMP;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.INSTANT_AS_MILLIS;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.INSTANT_AS_SECONDS;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.INSTANT_AS_TIMESTAMP;

public class InstantTest {
    private QueryConfiguration query;
    private PostgresDatabase.Database db;

    @Test
    public void asSeconds() {
        Instant now = Instant.now();
        query.query("INSERT INTO time_test(as_epoch_seconds) VALUES (?)")
             .single(Call.of().bind(now, INSTANT_AS_SECONDS))
             .insert();

        var res = query.query("SELECT as_epoch_seconds FROM time_test")
                       .single()
                       .map(row -> row.get(1, INSTANT_FROM_SECONDS))
                       .first()
                       .get();
        Assertions.assertEquals(now.getEpochSecond(), res.getEpochSecond());
    }

    @Test
    public void asMillis() {
        Instant now = Instant.now();
        query.query("INSERT INTO time_test(as_epoch_millis) VALUES (?)")
             .single(Call.of().bind(now, INSTANT_AS_MILLIS))
             .insert();

        var res = query.query("SELECT as_epoch_millis FROM time_test")
                       .single()
                       .map(row -> row.get(1, INSTANT_FROM_MILLIS))
                       .first()
                       .get();
        Assertions.assertEquals(now.toEpochMilli(), res.toEpochMilli());
    }

    @Test
    public void asTimestamp() {
        Instant now = Instant.now();
        query.query("INSERT INTO time_test(as_timestamp) VALUES (?)")
             .single(Call.of().bind(now, INSTANT_AS_TIMESTAMP))
             .insert();

        var res = query.query("SELECT as_timestamp FROM time_test")
                       .single()
                       .map(row -> row.get(1, INSTANT_FROM_TIMESTAMP))
                       .first()
                       .get();
        Assertions.assertEquals(now.toEpochMilli(), res.toEpochMilli());
    }

    @BeforeEach
    void before() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())
                                                                                                           .register(RowMapper.forClass(User.class).mapper(User.map()).build())).build();
    }

    @AfterEach
    void afterAll() {
        db.close();
    }
}
