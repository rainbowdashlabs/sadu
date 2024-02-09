/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu;

import de.chojo.sadu.databases.PostgreSql;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.updater.SqlUpdater;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class PostgresDatabase {
    public static Database createContainer(String user, String pw) throws IOException, SQLException {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", user)
                .withEnv("POSTGRES_PASSWORD", pw)
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));
        self.start();

        DataSource dc = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(self.getHost()).port(self.getFirstMappedPort())).create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();

        SqlUpdater.builder(dc, PostgreSql.get())
                .execute();

        return new Database(self, dc);
    }

    public static record Database(GenericContainer<?> container, DataSource dataSource) {
        public void close() {
            container.close();
        }
    }
}
