/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.wrapper.util.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static de.chojo.sadu.mapper.util.Results.columnNames;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class representing the mapping of a column to an object.
 *
 * @param <T> type of retuned object
 */
public class RowMapper<T> {
    private static final Logger log = LoggerFactory.getLogger(RowMapper.class);
    private final Class<T> clazz;
    private final ThrowingFunction<? extends T, Row, SQLException> mapper;
    private final Set<String> columns;

    RowMapper(Class<T> clazz, ThrowingFunction<? extends T, Row, SQLException> mapper, Set<String> columns) {
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

    public Set<String> columns() {
        return columns;
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
        return applicable(resultSet, MapperConfig.DEFAULT);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet result set
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSet resultSet, MapperConfig config) throws SQLException {
        return applicable(resultSet.getMetaData(), config);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param meta meta of a result set
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSetMetaData meta) {
        return applicable(meta, MapperConfig.DEFAULT);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param meta   meta of a result set
     * @param config mapper config
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSetMetaData meta, MapperConfig config) {
        Set<String> names;
        try {
            names = columnNames(meta);
        } catch (SQLException e) {
            log.error("Could not read columns", e);
            return 0;
        }

        var columns = new HashSet<>(this.columns);

        for (var entry : config.aliases().entrySet()) {
            if (columns.remove(entry.getKey())) columns.add(entry.getValue());
        }

        int size = names.size();
        if (columns.size() > size) {
            // The result set has less rows than we need
            return 0;
        }

        Set<String> overlap = new HashSet<>(names);
        overlap.retainAll(columns);

        if (overlap.size() != columns.size()) {
            // The result set has not all rows we need.
            return 0;
        }

        // Check that the result set has the same size as the expected columns when strict mode is enabled.
        if (config.isStrict() && size != columns.size()) {
            return 0;
        }

        return overlap.size();
    }


    @Override
    public String toString() {
        return "RowMapper{clazz=%s, columns=%s}".formatted(clazz.getName(), String.join(", ", columns));
    }
}
