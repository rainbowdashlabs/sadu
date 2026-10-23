/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import com.zaxxer.hikari.HikariConfig;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.mariadb.databases.MariaDb;
import de.chojo.sadu.mariadb.jdbc.MariaDbJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mariadb.jdbc.Configuration;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static de.chojo.sadu.MariaDbDatabase.createContainer;

class MariaDbJdbcTest {

    private static List<Arguments> createCredentials() {
        return List.of(
                Arguments.arguments("root", "abc"), // letters
                Arguments.arguments("root", "54871"), // numbers
                Arguments.arguments("root", "x89jwWpE"), // alphanumeric
                Arguments.arguments("root", "x89j&wW&pE"), // everything that cold break
                Arguments.arguments("root", "x89j!wW!pE"),
                Arguments.arguments("root", "x89j=wW=pE"),
                Arguments.arguments("root", "x89j$wW$pE"));
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void connectionTestWithDatasource(String user, String pw) {
        try (var container = createContainer(user, pw)) {
            DataSourceCreator.create(MariaDb.get())
                    .configure(c -> c.host(container.getHost()).port(container.getFirstMappedPort()))
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
            DataSourceCreator.create(MariaDb.get())
                    .configure(c -> c.host(container.getHost())
                            .port(container.getFirstMappedPort())
                            .user(user)
                            .password(pw))
                    .create()
                    .build();
        }
    }

    @ParameterizedTest
    @MethodSource("createCredentials")
    public void credentialsStayOutOfTheUrl(String user, String pw) {
        var config = hikariConfig(c -> c.user(user).password(pw));

        Assertions.assertEquals("jdbc:mariadb://localhost/", config.getJdbcUrl());
        Assertions.assertEquals(user, config.getUsername());
        Assertions.assertEquals(pw, config.getPassword());
    }

    @Test
    public void parameterValueReachesTheDriverUnchanged() throws SQLException {
        var value = "a&b=c d+é%/?#";
        var config = hikariConfig(c -> c.serverSslCert(value));

        var parsed = Configuration.parse(config.getJdbcUrl(), config.getDataSourceProperties());

        Assertions.assertEquals("jdbc:mariadb://localhost/", config.getJdbcUrl());
        Assertions.assertEquals(value, parsed.serverSslCert());
    }

    @Test
    public void parametersApplyOnTheConnection() throws SQLException {
        try (var container = createContainer("root", "root");
                var dataSource = DataSourceCreator.create(MariaDb.get())
                        .configure(c -> c.host(container.getHost())
                                .port(container.getFirstMappedPort())
                                .login("root", "root")
                                .addParameter("sessionVariables", "sql_mode='ANSI_QUOTES'"))
                        .create()
                        .build();
                var connection = dataSource.getConnection();
                var result = connection.createStatement().executeQuery("SELECT @@SESSION.sql_mode")) {
            result.next();
            Assertions.assertEquals("ANSI_QUOTES", result.getString(1));
        }
    }

    private static HikariConfig hikariConfig(Consumer<MariaDbJdbc> jdbc) {
        var config = new AtomicReference<HikariConfig>();
        DataSourceCreator.create(MariaDb.get()).configure(jdbc).create().editHikariConfig(config::set);
        return config.get();
    }
}
