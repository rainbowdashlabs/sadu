/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.jackson.JacksonRowMapperRegistry;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.updater.SqlUpdater;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class PostgresDatabase {
    public static Database createContainer() throws IOException, SQLException {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", "postgres")
                .withEnv("POSTGRES_PASSWORD", "postgres")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));
        self.start();

        DataSource dc = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(self.getHost()).port(self.getFirstMappedPort())).create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();

        SqlUpdater.builder(dc, PostgreSql.get())
                .execute();

        RowMapperRegistry registry = new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper());
        JacksonRowMapperRegistry jacksonRegistry = new JacksonRowMapperRegistry(new ObjectMapper().findAndRegisterModules(), registry);

        QueryConfiguration.setDefault(QueryConfiguration.builder(dc)
                .setRowMapperRegistry(jacksonRegistry)
                .build());

        return new Database(self, dc);
    }

    public static record Database(GenericContainer<?> container, DataSource dataSource) {
        public void close() {
            container.close();
        }
    }
}
