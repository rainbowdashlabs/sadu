/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.datasource;

import de.chojo.sqlutil.databases.SqlType;
import de.chojo.sqlutil.jdbc.SqLiteJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DataSourceCreatorTest {

    // These tests can not be executed without a running database sadly.

    //@Test
    public void postgresTest() throws SQLException {
        var build = DataSourceCreator.create(SqlType.POSTGRES)
                .configure(builder -> builder
                        .host("localhost")
                        .database("postgres")
                        .port(5432)
                        .user("postgres")
                        .password("root"))
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

    //@Test
    public void mariadbTest() throws SQLException {
        var build = DataSourceCreator.create(SqlType.MARIADB)
                .configure(builder -> builder
                        .host("localhost")
                        .database("public")
                        .port(3306)
                        .user("root")
                        .password("root"))
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

    @Test
    public void sqliteTest() throws SQLException {
        var build = DataSourceCreator.create(SqlType.SQLITE)
                .configure(SqLiteJdbc::memory)
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

}
