/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.util;

import de.chojo.sadu.core.types.SqlType;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Utility class to handle and extract data from {@link ResultSet}s and {@link ResultSetMetaData}
 */
public final class Results {
    private Results() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Get a set of column names in this result set
     *
     * @param set result set.
     * @return set of column names.
     * @throws SQLException if a database access error occurs.
     */
    public static Set<String> columnNames(ResultSet set) throws SQLException {
        return columnNames(set.getMetaData());
    }

    /**
     * Get a set of column names in this result set
     *
     * @param meta result set meta.
     * @return set of column names.
     * @throws SQLException if a database access error occurs.
     */
    public static Set<String> columnNames(ResultSetMetaData meta) throws SQLException {
        Set<String> columns = new HashSet<>();
        for (var i = 1; i <= meta.getColumnCount(); i++) {
            columns.add(meta.getColumnLabel(i));
        }
        return columns;
    }

    /**
     * Extracts the column index (1 based) of the first column with the requested type.
     *
     * @param meta  meta of the result set
     * @param types a list of valid types where the index will be returned
     * @return the index of the column with the first type contained in types
     * @throws SQLException if a database access error occurs
     */
    public static Optional<Integer> getFirstColumnIndexOfType(ResultSetMetaData meta, List<SqlType> types) throws SQLException {
        for (var i = 1; i <= meta.getColumnCount(); i++) {
            var typeName = meta.getColumnTypeName(i);
            for (var type : types) {
                if (typeName.equalsIgnoreCase(type.name())) return Optional.of(i);
                for (var alias : type.alias()) {
                    if (typeName.equalsIgnoreCase(alias)) return Optional.of(i);
                }
            }
        }
        return Optional.empty();
    }

    public static Set<String> columnTypes(ResultSetMetaData meta) throws SQLException {
        Set<String> columns = new HashSet<>();
        for (var i = 1; i <= meta.getColumnCount(); i++) {
            columns.add("%s (%s)".formatted(meta.getColumnTypeName(i), meta.getColumnType(i)));
        }
        return columns;
    }
}
