/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper.builder;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.wrapper.mapper.RowMapper;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RowMapperBuilder<T> implements PartialRowMapper<T> {
    private final Class<T> clazz;
    private ThrowingFunction<T, Row, SQLException> mapper;
    private final Set<String> columns = new HashSet<>();

    public RowMapperBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public RowMapperBuilder<T> setMapper(ThrowingFunction<T, Row, SQLException> mapper) {
        this.mapper = mapper;
        return this;
    }

    public RowMapperBuilder<T> addColumn(String column) {
        columns.add(column);
        return this;
    }

    public RowMapperBuilder<T> addColumns(String... columns) {
        this.columns.addAll(List.of(columns));
        return this;
    }

    public RowMapper<T> build() {
        return new RowMapper<>(clazz, mapper, columns);
    }
}
