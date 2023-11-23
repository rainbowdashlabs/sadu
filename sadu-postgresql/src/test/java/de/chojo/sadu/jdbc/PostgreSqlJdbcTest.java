/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import de.chojo.sadu.databases.PostgreSql;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

class PostgreSqlJdbcTest {

    private static List<Arguments> createCredentials() {
        return List.of(
                Arguments.arguments("postgres", "abc"), // letters
                Arguments.arguments("postgres", "54871"), // numbers
                Arguments.arguments("postgres", "x89jwWpE"), //alphanumeric
                Arguments.arguments("postgres", "x89j&wWpE"), // everything that cold break
                Arguments.arguments("postgres", "x89j!wWpE"),
                Arguments.arguments("postgres", "x89j=wWpE"),
                Arguments.arguments("postgres", "x89j$wWpE")
        );
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithDatasource(String username, String password) {
        try (var container = createContainer(username, password)) {
            DataSourceCreator.create(PostgreSql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort()))
                    .create()
                    .usingUsername(username)
                    .usingPassword(password)
                    .build();
        }
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithConfig(String username, String password) {
        try (var container = createContainer(username, password)) {
            DataSourceCreator.create(PostgreSql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(username)
                            .password(password))
                    .create()
                    .build();
        }
    }

    private GenericContainer<?> createContainer(String username, String password) {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", username)
                .withEnv("POSTGRES_PASSWORD", password);
        self.start();
        return self;
    }
}
