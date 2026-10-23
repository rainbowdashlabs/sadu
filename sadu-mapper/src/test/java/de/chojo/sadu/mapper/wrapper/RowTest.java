/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.wrapper;

import de.chojo.sadu.mapper.MapperConfig;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RowTest {
    @Test
    void getListUsesAliasesAndKeepsNullElements() throws SQLException {
        var row = new Row(
                resultSetWithArray("tags", new String[] {"a", null}), new MapperConfig().addAlias("labels", "tags"));

        List<String> list = row.getList("labels");

        assertEquals(Arrays.asList("a", null), list);
    }

    private static ResultSet resultSetWithArray(String column, Object[] elements) {
        var array = (Array) Proxy.newProxyInstance(
                RowTest.class.getClassLoader(), new Class<?>[] {Array.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getArray")) return elements;
                    throw new UnsupportedOperationException(method.getName());
                });
        return (ResultSet) Proxy.newProxyInstance(
                RowTest.class.getClassLoader(), new Class<?>[] {ResultSet.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getArray") && column.equals(args[0])) return array;
                    throw new SQLException("No column " + args[0]);
                });
    }
}
