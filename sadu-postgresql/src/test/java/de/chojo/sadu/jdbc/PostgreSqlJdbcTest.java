/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import de.chojo.sadu.databases.PostgreSql;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.StartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
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
    public void connectionTestWithDatasource(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(PostgreSql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort()))
                    .create()
                    .usingUsername(user)
                    .usingPassword(pw)
                    .build();
        }
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithConfig(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(PostgreSql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(user)
                            .password(pw))
                    .create()
                    .build();
        }
    }

    private GenericContainer<?> createContainer(String user, String pw) {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("postgres:latest"))
                .withExposedPorts(5432)
                .withEnv("POSTGRES_USER", user)
                .withEnv("POSTGRES_PASSWORD", pw)
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 2));
        self.start();
        return self;
    }

}
