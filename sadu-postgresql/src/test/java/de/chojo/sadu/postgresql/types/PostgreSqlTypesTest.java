/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.postgresql.types;

import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.mapper.util.Results;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Proxy;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostgreSqlTypesTest {
    static Stream<Arguments> reportedTypeNames() {
        return Stream.of(
                Arguments.of("int2", PostgreSqlTypes.SMALLINT),
                Arguments.of("int4", PostgreSqlTypes.INTEGER),
                Arguments.of("int8", PostgreSqlTypes.BIGINT));
    }

    @ParameterizedTest
    @MethodSource("reportedTypeNames")
    void matchesTheTypeNameTheDriverReports(String typeName, SqlType type) throws SQLException {
        assertEquals(Optional.of(1), Results.getFirstColumnIndexOfType(columnOfType(typeName), List.of(type)));
    }

    @ParameterizedTest
    @MethodSource("reportedTypeNames")
    void matchesNoOtherIntegerType(String typeName, SqlType type) throws SQLException {
        var others = Stream.of(PostgreSqlTypes.SMALLINT, PostgreSqlTypes.INTEGER, PostgreSqlTypes.BIGINT)
                .filter(other -> other != type)
                .toList();

        assertEquals(Optional.empty(), Results.getFirstColumnIndexOfType(columnOfType(typeName), others));
    }

    private static ResultSetMetaData columnOfType(String typeName) {
        return (ResultSetMetaData) Proxy.newProxyInstance(
                PostgreSqlTypesTest.class.getClassLoader(),
                new Class<?>[] {ResultSetMetaData.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getColumnCount" -> 1;
                    case "getColumnTypeName" -> typeName;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
