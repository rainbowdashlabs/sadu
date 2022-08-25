/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RowMapperBuilder<T> implements PartialRowMapper<T> {
    private final Class<T> clazz;
    private final Set<String> columns = new HashSet<>();
    private ThrowingFunction<? extends T, Row, SQLException> mapper;

    RowMapperBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RowMapperBuilder<T> mapper(ThrowingFunction<? extends T, Row, SQLException> mapper) {
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
        return new RowMapper<>(clazz, mapper, columns);
    }
}
