/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil;

import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//TODO: Setup testcontainers properly with workflow
@Testcontainers
public class TestContainers {

    @Container
    public static final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:latest")
            .withDatabaseName("default")
            .withUsername("root")
            .withPassword("root");

    @Container
    public static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("default")
            .withUsername("root")
            .withPassword("root");

    //@Test
    public void testRunning() {
        Assertions.assertTrue(POSTGRE_SQL_CONTAINER.isRunning());
        Assertions.assertTrue(MARIA_DB_CONTAINER.isRunning());
    }
}
