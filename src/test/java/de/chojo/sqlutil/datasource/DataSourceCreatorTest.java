/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.datasource;

import org.junit.jupiter.api.Assertions;
import org.mariadb.jdbc.MariaDbDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;

class DataSourceCreatorTest {

    // These tests can be executed without a running database sadly.

    //@Test
    public void postgresTest() throws SQLException {
        var build = DataSourceCreator.create(PGSimpleDataSource.class)
                .withAddress("localhost")
                .forDatabase("postgres")
                .withPort(5432)
                .withUser("postgres")
                .withPassword("root")
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

    //@Test
    public void mariadbTest() throws SQLException {
        var build = DataSourceCreator.create(MariaDbDataSource.class)
                .withAddress("localhost")
                .forDatabase("test_db")
                .withPort(3306)
                .withUser("root")
                .withPassword("root")
                .create()
                .withMaximumPoolSize(20)
                .withMinimumIdle(2)
                .build();

        Assertions.assertTrue(build.getConnection().isValid(1000));
    }

}
