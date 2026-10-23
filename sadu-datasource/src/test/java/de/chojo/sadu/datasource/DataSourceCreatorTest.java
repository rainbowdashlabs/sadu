/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.datasource.exceptions.DriverClassNotFoundException;
import de.chojo.sadu.sqlite.databases.SqLite;
import de.chojo.sadu.sqlite.jdbc.SqLiteJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteConnection;

import java.sql.SQLException;

class DataSourceCreatorTest {

    @Test
    public void sqliteTest() throws SQLException {
        // Create a datasource for sqlite
        HikariDataSource build = DataSourceCreator.create(SqLite.sqlite())
                .configure(SqLiteJdbc::memory)
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

    @Test
    public void missingDriverNamesTheDriverClassMethod() {
        var exception = Assertions.assertThrows(
                DriverClassNotFoundException.class,
                () -> DataSourceCreator.create(SqLite.sqlite())
                        .configure(config -> config.memory().driverClass("does.not.Exist"))
                        .create());

        Assertions.assertTrue(exception.getMessage().contains("#driverClass"), exception.getMessage());
    }

    @Test
    public void sqliteParametersReachTheDriverUnchanged() throws SQLException {
        var format = "yyyy-MM-dd' a&b=c d+é%/?#'";
        try (HikariDataSource dataSource = DataSourceCreator.create(SqLite.sqlite())
                        .configure(config -> config.memory().addParameter("date_string_format", format))
                        .create()
                        .build();
                var connection = dataSource.getConnection()) {
            Assertions.assertEquals("jdbc:sqlite::memory:", dataSource.getJdbcUrl());
            Assertions.assertEquals(
                    format,
                    connection
                            .unwrap(SQLiteConnection.class)
                            .getConnectionConfig()
                            .getDateStringFormat());
        }
    }
}
