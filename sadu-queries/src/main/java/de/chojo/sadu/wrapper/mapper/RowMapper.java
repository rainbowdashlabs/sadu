/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.wrapper.mapper.builder.PartialRowMapper;
import de.chojo.sadu.wrapper.mapper.builder.RowMapperBuilder;
import de.chojo.sadu.wrapper.util.Row;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Set;

import static de.chojo.sadu.wrapper.mapper.util.Result.columnNames;
import static org.slf4j.LoggerFactory.getLogger;

public class RowMapper<T> {
    private static final Logger log = getLogger(RowMapper.class);
    private final Class<T> clazz;
    private final ThrowingFunction<T, Row, SQLException> mapper;
    private final Set<String> columns;

    public RowMapper(Class<T> clazz, ThrowingFunction<T, Row, SQLException> mapper, Set<String> columns) {
        this.clazz = clazz;
        this.mapper = mapper;
        this.columns = columns;
    }

    public static <T> PartialRowMapper<T> forClass(Class<T> clazz) {
        return new RowMapperBuilder<>(clazz);
    }

    public Class<T> clazz() {
        return clazz;
    }

    public T map(Row row) throws SQLException {
        return mapper.apply(row);
    }

    public boolean isWildcard() {
        return columns.isEmpty();
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet result set
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSet resultSet) throws SQLException {
        return applicable(resultSet.getMetaData());
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet meta of a result set
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSetMetaData resultSet) {
        Set<String> names;
        try {
            names = columnNames(resultSet);
        } catch (SQLException e) {
            log.error("Could not read columns", e);
            return 0;
        }
        int size = names.size();
        if (columns.size() > size) {
            // The result set has less rows than we need
            return 0;
        }
        names.retainAll(columns);

        if (names.size() != columns.size()) {
            // The result set has not all rows we need.
            return 0;
        }
        return names.size();
    }


    @Override
    public String toString() {
        return "RowMapper{clazz=%s, columns=%s}".formatted(clazz, String.join(", ", columns));
    }
}
