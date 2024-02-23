/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.sqlite.databases.SqLite;
import de.chojo.sadu.sqlite.jdbc.SqLiteJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}
