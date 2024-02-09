/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import de.chojo.sadu.mariadb.databases.MariaDb;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mariadb.jdbc.MariaDbJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static de.chojo.sadu.MariaDbDatabase.createContainer;

class MariaDbJdbcTest {

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

    private static List<Arguments> escapeTest() {
        return List.of(
                Arguments.arguments("root", "abc", "jdbc:mariadb://localhost/?user=root&password=abc"), // letters
                Arguments.arguments("root", "54871", "jdbc:mariadb://localhost/?user=root&password=54871"), // numbers
                Arguments.arguments("root", "x89jwWpE", "jdbc:mariadb://localhost/?user=root&password=x89jwWpE"), //alphanumeric
                Arguments.arguments("root", "x89j&wW&pE", "jdbc:mariadb://localhost/?user=root&password=x89j%26wW%26pE"), // everything that could break
                Arguments.arguments("root", "x89j!wW!pE", "jdbc:mariadb://localhost/?user=root&password=x89j%21wW%21pE"),
                Arguments.arguments("root", "x89j=wW=pE", "jdbc:mariadb://localhost/?user=root&password=x89j%3DwW%3DpE"),
                Arguments.arguments("root", "x89j$wW$pE", "jdbc:mariadb://localhost/?user=root&password=x89j%24wW%24pE")
        );
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithDatasource(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(MariaDb.get())
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
            DataSourceCreator.create(MariaDb.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(user).password(pw))
                    .create()
                    .build();
        }
    }


    @ParameterizedTest
    @MethodSource("escapeTest")
    public void escape(String user, String password, String result) {
        var url = new MariaDbJdbc()
                .user(user)
                .password(password)
                .jdbcUrl();

        Assertions.assertEquals(result, url);
    }
}
