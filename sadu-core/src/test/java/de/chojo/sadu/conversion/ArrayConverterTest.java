/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.conversion;

import de.chojo.sadu.core.conversion.ArrayConverter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayConverterTest {
    @Test
    void toSetHoldsTheElements() throws SQLException {
        Set<String> set = ArrayConverter.toSet(resultSetWithArray(new String[] {"a", "b", "a"}), "values");

        assertEquals(Set.of("a", "b"), set);
    }

    static ResultSet resultSetWithArray(Object[] elements) {
        var array = (Array) Proxy.newProxyInstance(
                ArrayConverterTest.class.getClassLoader(), new Class<?>[] {Array.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getArray")) return elements;
                    throw new UnsupportedOperationException(method.getName());
                });
        return (ResultSet) Proxy.newProxyInstance(
                ArrayConverterTest.class.getClassLoader(), new Class<?>[] {ResultSet.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getArray")) return array;
                    throw new UnsupportedOperationException(method.getName());
                });
    }
}
