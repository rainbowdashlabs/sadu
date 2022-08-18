/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.jdbc.SqLiteJdbc;
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
