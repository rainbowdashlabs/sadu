/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static de.chojo.sadu.PostgresDatabase.createContainer;

class PostgreSqlJdbcTest {

    private static List<Arguments> createCredentials() {
        return List.of(
                Arguments.arguments("postgres", "abc"), // letters
                Arguments.arguments("postgres", "54871"), // numbers
                Arguments.arguments("postgres", "x89jwWpE"), //alphanumeric
                Arguments.arguments("postgres", "x89j&wW&pE"), // everything that could break
                Arguments.arguments("postgres", "x89j!wW!pE"),
                Arguments.arguments("postgres", "x89j=wW=pE"),
                Arguments.arguments("postgres", "x89j$wW$pE")
        );
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithDatasource(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(PostgreSql.get())
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
            DataSourceCreator.create(PostgreSql.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(user).password(pw))
                    .create()
                    .build();
        }
    }
}
