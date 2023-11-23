/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import de.chojo.sadu.databases.MySql;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

class MySQLJdbcTest {
    private static List<Arguments> createCredentials() {
        return List.of(
                Arguments.arguments("root", "abc"), // letters
                Arguments.arguments("root", "54871"), // numbers
                Arguments.arguments("root", "x89jwWpE"), //alphanumeric
                Arguments.arguments("root", "x89j&wW&pE"), // everything that cold break
                Arguments.arguments("root", "x89j!wW!pE"),
                Arguments.arguments("root", "x89j=wW=pE"),
                Arguments.arguments("root", "x89j$wW$pE")
        );
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithDatasource(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(MySql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort()))
                    .create().usingUsername(user)
                    .usingPassword(pw)
                    .build();
        }
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithConfig(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(MySql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(user).password(pw))
                    .create()
                    .build();
        }
    }

    private GenericContainer<?> createContainer(String user/*ignored*/, String pw) {
        GenericContainer<?> self = new GenericContainer<>(DockerImageName.parse("mysql:latest"))
                .withExposedPorts(3306)
                .withEnv("MYSQL_ROOT_PASSWORD", pw)
                .waitingFor(Wait.forLogMessage(".*/usr/sbin/mysqld: ready for connections\\..*", 2));
        self.start();
        return self;
    }

    private static List<Arguments> escapeTest() {
        return List.of(
                Arguments.arguments("root", "abc", "jdbc:mysql://localhost/?user=root&password=abc"), // letters
                Arguments.arguments("root", "54871", "jdbc:mysql://localhost/?user=root&password=54871"), // numbers
                Arguments.arguments("root", "x89jwWpE", "jdbc:mysql://localhost/?user=root&password=x89jwWpE"), //alphanumeric
                Arguments.arguments("root", "x89j&wW&pE", "jdbc:mysql://localhost/?user=root&password=x89j%26wW%26pE"), // everything that could break
                Arguments.arguments("root", "x89j!wW!pE", "jdbc:mysql://localhost/?user=root&password=x89j%21wW%21pE"),
                Arguments.arguments("root", "x89j=wW=pE", "jdbc:mysql://localhost/?user=root&password=x89j%3DwW%3DpE"),
                Arguments.arguments("root", "x89j$wW$pE", "jdbc:mysql://localhost/?user=root&password=x89j%24wW%24pE")
        );
    }

    @ParameterizedTest
    @MethodSource("escapeTest")
    public void escape(String user, String password, String result) {
        var url = new MySQLJdbc()
                .user(user)
                .password(password)
                .jdbcUrl();

        Assertions.assertEquals(result, url);
    }

}
