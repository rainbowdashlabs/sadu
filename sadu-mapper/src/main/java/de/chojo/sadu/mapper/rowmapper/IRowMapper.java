/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public interface IRowMapper<T> extends RowMapping<T> {
    @Override
    T map(Row row) throws SQLException;

    T map(Row row, int index) throws SQLException;

    boolean isWildcard();

    int applicable(ResultSet resultSet) throws SQLException;

    int applicable(ResultSet resultSet, MapperConfig config) throws SQLException;

    int applicable(ResultSetMetaData meta) throws SQLException;

    int applicable(ResultSetMetaData meta, MapperConfig config) throws SQLException;

    default List<SqlType> types(){
        return Collections.emptyList();
    }
}
