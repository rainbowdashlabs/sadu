/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MetaResult extends Result {
    String meta;

    public MetaResult(int id, String result, String meta) {
        super(id, result);
        this.meta = meta;
    }

    public static ResultSet fullResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("result")).thenReturn("result");
        when(resultSet.getString("meta")).thenReturn("meta");

        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(meta.getColumnCount()).thenReturn(3);
        when(meta.getColumnLabel(1)).thenReturn("id");
        when(meta.getColumnLabel(2)).thenReturn("result");
        when(meta.getColumnLabel(3)).thenReturn("meta");

        when(resultSet.getMetaData()).thenReturn(meta);
        return resultSet;
    }

    public static ResultSet sparseResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("result")).thenReturn("result");

        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(meta.getColumnCount()).thenReturn(2);
        when(meta.getColumnLabel(1)).thenReturn("id");
        when(meta.getColumnLabel(2)).thenReturn("result");

        when(resultSet.getMetaData()).thenReturn(meta);
        return resultSet;
    }

    public static ResultSet aliasedResultSet() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("r_result")).thenReturn("result");

        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(meta.getColumnCount()).thenReturn(2);
        when(meta.getColumnLabel(1)).thenReturn("id");
        when(meta.getColumnLabel(2)).thenReturn("r_result");

        when(resultSet.getMetaData()).thenReturn(meta);
        return resultSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaResult)) return false;

        MetaResult metaResult1 = (MetaResult) o;

        if (id != metaResult1.id) return false;
        if (!result.equals(metaResult1.result)) return false;
        return Objects.equals(meta, metaResult1.meta);
    }

    @Override
    public int hashCode() {
        int result1 = id;
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + (meta != null ? meta.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "Result{id=%d, result='%s', meta='%s'}".formatted(id, result, meta);
    }
}
