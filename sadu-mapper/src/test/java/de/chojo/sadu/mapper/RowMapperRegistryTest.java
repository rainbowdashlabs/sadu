/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.mapper.annotation.MappingProvider;
import de.chojo.sadu.mapper.exceptions.InvalidMappingException;
import de.chojo.sadu.mapper.exceptions.MappingException;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.mapper.wrapper.Row;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.ResultSetMetaData;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RowMapperRegistryTest {
    private final RowMapper<String> byName = RowMapper.forClass(String.class)
            .mapper(row -> "name")
            .addColumn("name")
            .build();
    private final RowMapper<String> wildcard =
            RowMapper.forClass(String.class).mapper(row -> "wildcard").build();

    @Test
    void usesWildcardWhenNoMapperMatches() throws Exception {
        var registry = new RowMapperRegistry().register(byName, wildcard);

        assertSame(wildcard, registry.findOrWildcard(String.class, meta("other"), MapperConfig.DEFAULT));
    }

    @Test
    void prefersMatchingMapperOverWildcard() throws Exception {
        var registry = new RowMapperRegistry().register(byName, wildcard);

        assertSame(byName, registry.findOrWildcard(String.class, meta("name"), MapperConfig.DEFAULT));
    }

    @Test
    void findsNothingWhenNoMapperMatches() {
        var registry = new RowMapperRegistry().register(byName);

        assertTrue(
                registry.find(String.class, meta("other"), MapperConfig.DEFAULT).isEmpty());
        assertThrows(
                MappingException.class,
                () -> registry.findOrWildcard(String.class, meta("other"), MapperConfig.DEFAULT));
    }

    @Test
    void strictUsesWildcardForExtraColumns() throws Exception {
        var registry = new RowMapperRegistry().register(byName, wildcard);

        assertSame(wildcard, registry.findOrWildcard(String.class, meta("name", "other"), new MapperConfig().strict()));
    }

    @Test
    void constructorFailureKeepsItsCause() throws Exception {
        var registry = new RowMapperRegistry();
        registry.register(FailingConstructor.class);
        var mapper = registry.findOrWildcard(FailingConstructor.class, meta("value"), MapperConfig.DEFAULT);

        var exception = assertThrows(InvalidMappingException.class, () -> mapper.map(null));

        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }

    @Test
    void discoveringAMethodMappingTwiceKeepsTheFirst() throws Exception {
        var registry = new RowMapperRegistry();
        registry.register(MethodMapped.class);
        var first = registry.findOrWildcard(MethodMapped.class, meta("value"), MapperConfig.DEFAULT);

        assertDoesNotThrow(() -> registry.register(MethodMapped.class));
        assertSame(first, registry.findOrWildcard(MethodMapped.class, meta("value"), MapperConfig.DEFAULT));
    }

    private static ResultSetMetaData meta(String... columns) {
        return (ResultSetMetaData) Proxy.newProxyInstance(
                RowMapperRegistryTest.class.getClassLoader(),
                new Class<?>[] {ResultSetMetaData.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getColumnCount" -> columns.length;
                    case "getColumnLabel", "getColumnName" -> columns[(int) args[0] - 1];
                    case "getColumnTypeName" -> "text";
                    case "getColumnType" -> Types.VARCHAR;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    public static class FailingConstructor {
        @MappingProvider("value")
        public FailingConstructor(Row row) {
            throw new IllegalStateException("broken");
        }
    }

    public static class MethodMapped {
        @MappingProvider("value")
        public static RowMapping<MethodMapped> map() {
            return row -> new MethodMapped();
        }
    }
}
