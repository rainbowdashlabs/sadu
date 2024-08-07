/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.postgresql.databases.PostgreSql;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.api.query.Query;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static de.chojo.sadu.PostgresDatabase.createContainer;

class PostgresqlMapperTest {
    @Language("postgresql")
    static final String selectBigInt = "SELECT 9223372036854775807::BIGINT";
    @Language("postgresql")
    static final String selectInteger = "SELECT 2147483647::INTEGER";
    @Language("postgresql")
    static final String selectSmallInt = "SELECT 32767::SMALLINT";
    @Language("postgresql")
    static final String selectDecimal = "SELECT 3276745687654.87845::DECIMAL";
    @Language("postgresql")
    static final String selectNumeric = "SELECT 3276745687654.87845::NUMERIC";
    @Language("postgresql")
    static final String selectReal = "SELECT 3267.128::REAL";
    @Language("postgresql")
    static final String selectDouble = "SELECT 3276745687.1265487845::DOUBLE PRECISION";
    @Language("postgresql")
    static final String selectText = "SELECT 'text'::TEXT";
    @Language("postgresql")
    static final String selectVarchar = "SELECT 'text'::VARCHAR";
    @Language("postgresql")
    static final String selectChar = "SELECT 'text'::CHAR(4)";
    @Language("postgresql")
    static final String selectJson = "SELECT '{\"a\": \"b\"}'::JSON";
    @Language("postgresql")
    static final String selectJsonB = "SELECT '{\"a\": \"b\"}'::JSONB";
    static final long maxLong = 9223372036854775807L;
    static final int maxInt = 2147483647;
    static final short maxShort = 32767;
    static final float floatVal = 3267.128f;
    static final double doubleVal = 3276745687.1265487845;
    static final BigDecimal bigDecimal = BigDecimal.valueOf(327674568765487845L, 5);
    static final String text = "text";
    static final String json = "{\"a\": \"b\"}";
    private static GenericContainer<?> pg;

    @BeforeAll
    static void beforeAll() throws IOException {
        pg = createContainer("postgres", "postgres");
        DataSource dc = DataSourceCreator.create(PostgreSql.get())
                .configure(c -> c.host(pg.getHost()).port(pg.getFirstMappedPort())).create()
                .usingPassword("postgres")
                .usingUsername("postgres")
                .build();
        QueryConfiguration.setDefault(QueryConfiguration.builder(dc)
                .setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper()))
                .build());
    }

    static Stream<Arguments> shortTestInput() {
        return Stream.of(
                // SMALL_INT
                Arguments.of(Short.class, selectSmallInt, maxShort),
                Arguments.of(Integer.class, selectSmallInt, (int) maxShort),
                Arguments.of(Long.class, selectSmallInt, (long) maxShort),
                // INTEGER
                Arguments.of(Integer.class, selectInteger, maxInt),
                Arguments.of(Long.class, selectInteger, (long) maxInt),
                // BIGINT
                Arguments.of(Long.class, selectBigInt, maxLong),
                // REAL
                Arguments.of(Float.class, selectReal, floatVal),
                Arguments.of(Double.class, selectReal, Double.parseDouble(String.valueOf(floatVal))), // this is very hacky, but it's the most precise way xD
                Arguments.of(BigDecimal.class, selectReal, BigDecimal.valueOf(Double.parseDouble(String.valueOf(floatVal)))),
                // DOUBLE PRECISION
                Arguments.of(Double.class, selectDouble, doubleVal),
                Arguments.of(BigDecimal.class, selectDouble, BigDecimal.valueOf(doubleVal)),
                // NUMERIC aka DECIMAL
                Arguments.of(BigDecimal.class, selectNumeric, bigDecimal),
                Arguments.of(BigDecimal.class, selectDecimal, bigDecimal),
                // TEXT
                Arguments.of(String.class, selectText, text),
                // VARCHAR
                Arguments.of(String.class, selectVarchar, text),
                // CHAR
                Arguments.of(String.class, selectChar, text),
                // JSONB
                Arguments.of(String.class, selectJsonB, json),
                // JSON
                Arguments.of(String.class, selectJson, json)
        );
    }

    @AfterAll
    static void afterAll() throws IOException {
        pg.close();
    }

    @ParameterizedTest
    @MethodSource("shortTestInput")
    <T> void testAutoParsing(Class<T> clazz, String query, T expected) {
        var val = Query.query(query)
                .single()
                .mapAs(clazz)
                .first();
        Assertions.assertTrue(val.isPresent());
        Assertions.assertEquals(expected, val.get());
    }
}
