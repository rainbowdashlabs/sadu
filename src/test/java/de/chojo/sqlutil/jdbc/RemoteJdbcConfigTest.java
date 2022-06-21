/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoteJdbcConfigTest {

    private RemoteJdbcConfig<RemoteJdbcConfig<?>> jdbc;

    @BeforeEach
    void setUp() {
        jdbc = new RemoteJdbcConfig<>() {
            @Override
            protected String driver() {
                return "driver";
            }

            @Override
            protected String defaultDriverClass() {
                return null;
            }
        };
    }

    @Test
    void testDatabase() {
        var jdbcUrl = jdbc.database("database").jdbcUrl();
        Assertions.assertEquals("jdbc:driver:database", jdbcUrl);
    }

    @Test
    void testNone() {
        var jdbcUrl = jdbc.jdbcUrl();
        Assertions.assertEquals("jdbc:driver:/", jdbcUrl);
    }

    @Test
    void testHostDatabase() {
        var jdbcUrl = jdbc.host("host").database("database").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host/database", jdbcUrl);
    }

    @Test
    void testHost() {
        var jdbcUrl = jdbc.host("host").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host/", jdbcUrl);
    }

    @Test
    void testPort() {
        jdbc.port(1234);
        Assertions.assertThrows(IllegalStateException.class, () -> jdbc.jdbcUrl());

        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.port(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.port("0"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.port("99999"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.port(99999));
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.port("abc"));

        var jdbcUrl = jdbc.host("host").port(1234).jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host:1234/", jdbcUrl);
    }

    @Test
    void testHostPort() {
        var jdbcUrl = jdbc.host("host").port("1234").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host:1234/", jdbcUrl);
    }

    @Test
    void testHostPortDatabase() {
        var jdbcUrl = jdbc.host("host").port("1234").database("database").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host:1234/database", jdbcUrl);
    }

    @Test
    void testIpv4() {
        var jdbcUrl = jdbc.ipv4("1.1.1.1").port("1234").database("database").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://1.1.1.1:1234/database", jdbcUrl);
    }

    @Test
    void testIpv6Format() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.ipv6("1:1"));
    }

    @Test
    void testIpv6() {
        var jdbcUrl = jdbc.ipv6("1:2:3:4:5:6:7:8").port("1234").database("database").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://[1:2:3:4:5:6:7:8]:1234/database", jdbcUrl);
    }

    @Test
    void testSpaceEscapeParameter() {
        var jdbcUrl = jdbc.host("host").addParameter("key", "value 1").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host/?key=value+1", jdbcUrl);
    }

    @Test
    void testEqualCharEscapeParameter() {
        var jdbcUrl = jdbc.host("host").addParameter("key2", "value=1").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host/?key2=value%3D1", jdbcUrl);
    }

    @Test
    void testReassigmentParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jdbc.addParameter("key", "value").addParameter("key", "value"));
    }

    @Test
    void testQueryEncodingParameter() {
        var jdbcUrl = jdbc.host("host").addParameter("key", "value&1").jdbcUrl();
        Assertions.assertEquals("jdbc:driver://host/?key=value%261", jdbcUrl);
    }
}
