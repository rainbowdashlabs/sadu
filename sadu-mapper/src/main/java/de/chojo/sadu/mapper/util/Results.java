/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.util;

import de.chojo.sadu.types.SqlType;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            columns.add(meta.getColumnLabel(i));
        }
        return columns;
    }

    public static Optional<Integer> getColumnIndexOfType(ResultSetMetaData meta, List<SqlType> types) throws SQLException {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String typeName = meta.getColumnTypeName(i);
            for (SqlType type : types) {
                if (typeName.equalsIgnoreCase(type.name())) return Optional.of(i);
                for (String alias : type.alias()) {
                    if (typeName.equalsIgnoreCase(alias)) return Optional.of(i);
                }
            }
        }
        return Optional.empty();
    }
}
