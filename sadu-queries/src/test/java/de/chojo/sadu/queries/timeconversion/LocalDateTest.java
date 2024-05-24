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
import java.time.LocalDateTime;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.converter.StandardValueConverter.LOCAL_DATE;

public class LocalDateTest {
    private QueryConfiguration query;
    private PostgresDatabase.Database db;

    @Test
    public void withoutTimezone() {
        var now = LocalDateTime.of(2000, 1, 1, 0, 0).toLocalDate();
        query.query("INSERT INTO time_test(as_date) VALUES (?)")
             .single(Call.of().bind(now, LOCAL_DATE))
             .insert();

        var res = query.query("SELECT as_date FROM time_test")
                       .single()
                       .map(row -> row.get(1, LOCAL_DATE))
                       .first()
                       .get();
        Assertions.assertEquals(now, res);
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