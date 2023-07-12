/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;

/**
 * Represents a partially configured {@link RowMapper}
 * @param <T> type of the mapper result.
 */
public interface PartialRowMapper<T> {
    /**
     * Adds a mapper to map a row to the required object.
     *
     * @param mapper mapper
     * @return builder instance
     */
    RowMapperBuilder<T> mapper(ThrowingFunction<? extends T, Row, SQLException> mapper);
}
