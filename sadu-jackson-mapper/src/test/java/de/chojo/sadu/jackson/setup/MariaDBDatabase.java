/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson.setup;

import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mariadb.databases.MariaDb;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import de.chojo.sadu.updater.SqlUpdater;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class MariaDBDatabase {
    public static Database createContainer() throws IOException, SQLException {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("mariadb:latest"))
                .withExposedPorts(3306)
                .withEnv("MARIADB_ROOT_PASSWORD", "root")
                .waitingFor(Wait.forLogMessage(".*mariadbd: ready for connections.*", 2));
        self.start();

        DataSource dc = DataSourceCreator.create(MariaDb.get())
                .configure(c -> c.host(self.getHost()).port(self.getFirstMappedPort())).create()
                .usingPassword("root")
                .usingUsername("root")
                .build();

        SqlUpdater.builder(dc, MariaDb.get())
                .execute();

        return new Database(self, dc);
    }

    public static record Database(GenericContainer<?> container, DataSource dataSource) {
        public void close() {
            container.close();
        }
    }
}
