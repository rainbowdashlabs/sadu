/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.core.exceptions.ThrowingBiFunction;
import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A builder to build a {@link RowMapper}.
 *
 * @param <T> type of the mapper result.
 */
public class RowMapperBuilder<T> implements PartialRowMapper<T> {
    private final Class<T> clazz;
    private final Set<String> columns = new HashSet<>();
    private RowMapping<T> mapper;
    private List<SqlType> types = Collections.emptyList();
    private ThrowingBiFunction<Row, Integer, T, SQLException> indexMapper;

    RowMapperBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RowMapperBuilder<T> mapper(RowMapping<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * Adds a column to the row mapper
     *
     * @param column column to add
     * @return builder instance
     */
    public RowMapperBuilder<T> addColumn(String column) {
        columns.add(column);
        return this;
    }

    /**
     * Adds columns to the row mapper.
     *
     * @param columns columns to add
     * @return builder instance
     */
    public RowMapperBuilder<T> addColumns(String... columns) {
        this.columns.addAll(List.of(columns));
        return this;
    }

    /**
     * Build the row mapper.
     *
     * @return new RowMapper instance
     */
    public RowMapper<T> build() {
        return new RowMapper<>(clazz, mapper, indexMapper, columns, types);
    }

    public RowMapperBuilder<T> types(List<SqlType> types) {
        this.types = Objects.requireNonNull(types);
        return this;
    }

    public RowMapperBuilder<T> indexMapper(ThrowingBiFunction<Row, Integer, T, SQLException> mapper) {
        this.indexMapper = mapper;
        return this;
    }
}
