/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public final class Result {
    private Result() {
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
}
