/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.rowmapper;

import de.chojo.sadu.core.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.SQLException;

public interface RowMapping<T> {
    static <V> RowMapping<V> create(ThrowingFunction<V, Row, SQLException> mapper) {
        return mapper::apply;
    }

    T map(Row row) throws SQLException;
}
