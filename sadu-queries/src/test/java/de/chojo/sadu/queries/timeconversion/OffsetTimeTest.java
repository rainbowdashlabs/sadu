/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.timeconversion;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.reader.StandardReader;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.call.adapter.StandardAdapter;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import de.chojo.sadu.queries.examples.dao.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;

import static de.chojo.sadu.PostgresDatabase.createContainer;

public class OffsetTimeTest {
    private QueryConfiguration query;
    private PostgresDatabase.Database db;

    @BeforeEach
    void before() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())
                                                                                                           .register(RowMapper.forClass(User.class).mapper(User.map()).build())).build();
    }

    @Test
    public void withoutTimezone() {
        var now = OffsetTime.now();
        query.query("INSERT INTO time_test(as_time) VALUES (?)")
             .single(Call.of().bind(now, StandardAdapter.OFFSET_TIME))
             .insert();

        var res = query.query("SELECT as_time FROM time_test")
                       .single()
                       .map(row -> row.get(1, StandardReader.OFFSET_TIME))
                       .first()
                       .get();
        Assertions.assertEquals(now.truncatedTo(ChronoUnit.SECONDS), res.truncatedTo(ChronoUnit.SECONDS));
    }

    @AfterEach
    void afterAll() {
        db.close();
    }
}
