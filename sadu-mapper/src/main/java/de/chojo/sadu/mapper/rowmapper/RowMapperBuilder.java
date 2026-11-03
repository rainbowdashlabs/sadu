/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A builder to build a {@link RowMapper}.
 *
 * @param <T> type of the mapper result.
 */
public class RowMapperBuilder<T> implements PartialRowMapper<T> {
    private static final Logger log = getLogger(RowMapperBuilder.class);
    private final Class<T> clazz;
    private final Set<String> columns = new HashSet<>();
    private RowMapping<T> mapper;

    RowMapperBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RowMapperBuilder<T> mapper(RowMapping<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * Adds a column to the row mapper. A blank column is ignored with a warning, so a mapper with only blank columns is
     * a wildcard.
     *
     * @param column column to add
     * @return builder instance
     */
    public RowMapperBuilder<T> addColumn(String column) {
        if (column.isBlank()) {
            log.warn(
                    "Ignoring blank column in mapper for {}. Add no column to define a wildcard mapper.",
                    clazz.getName());
            return this;
        }
        columns.add(column);
        return this;
    }

    /**
     * Adds columns to the row mapper. Blank columns are ignored with a warning, so a mapper with only blank columns is
     * a wildcard.
     *
     * @param columns columns to add
     * @return builder instance
     */
    public RowMapperBuilder<T> addColumns(String... columns) {
        for (String column : columns) addColumn(column);
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
