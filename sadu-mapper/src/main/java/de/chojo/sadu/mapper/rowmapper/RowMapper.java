/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.core.exceptions.ThrowingBiFunction;
import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.wrapper.Row;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.chojo.sadu.mapper.util.Results.columnNames;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class representing the mapping of a column to an object.
 *
 * @param <T> type of returned object
 */
public class RowMapper<T> implements IRowMapper<T> {
    private static final Logger log = getLogger(RowMapper.class);
    private final Class<T> clazz;
    private final RowMapping<T> mapper;
    private final Set<String> columns;
    private final List<SqlType> types;
    private final ThrowingBiFunction<Row, Integer, T, SQLException> indexMapper;

    RowMapper(Class<T> clazz, RowMapping<T> mapper, ThrowingBiFunction<Row, Integer, T, SQLException> indexMapper, Set<String> columns, List<SqlType> types) {
        this.clazz = clazz;
        this.mapper = mapper;
        this.columns = columns;
        this.types = types;
        this.indexMapper = indexMapper;
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

    @Override
    public T map(Row row) throws SQLException {
        return mapper.map(row);
    }

    @Override
    public T map(Row row, int index) throws SQLException {
        if(indexMapper == null) throw new UnsupportedOperationException("IndexMapper not set");
        return indexMapper.apply(row, index);
    }


    @Override
    public boolean isWildcard() {
        return columns.isEmpty();
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet result set
     * @return If the result set is not applicable 0 will be returned. Otherwise, the count of applicable rows will be returned.
     */
    @Override
    public int applicable(ResultSet resultSet) throws SQLException {
        return applicable(resultSet, MapperConfig.DEFAULT);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet result set
     * @return If the result set is not applicable 0 will be returned. Otherwise, the count of applicable rows will be returned.
     */
    @Override
    public int applicable(ResultSet resultSet, MapperConfig config) throws SQLException {
        return applicable(resultSet.getMetaData(), config);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param meta meta of a result set
     * @return If the result set is not applicable 0 will be returned. Otherwise, the count of applicable rows will be returned.
     */
    @Override
    public int applicable(ResultSetMetaData meta) {
        return applicable(meta, MapperConfig.DEFAULT);
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param meta   meta of a result set
     * @param config mapper config
     * @return If the result set is not applicable 0 will be returned. Otherwise, the count of applicable rows will be returned.
     */
    @Override
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

        var size = names.size();
        if (columns.size() > size) {
            // The result set has fewer rows than we need
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
    public List<SqlType> types() {
        return Collections.unmodifiableList(types);
    }

    @Override
    public String toString() {
        return "RowMapper{clazz=%s, columns=%s}".formatted(clazz.getName(), String.join(", ", columns));
    }
}
