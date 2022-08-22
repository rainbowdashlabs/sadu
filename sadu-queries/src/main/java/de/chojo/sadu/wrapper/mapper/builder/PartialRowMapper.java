/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper.builder;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;

public interface PartialRowMapper<T> {
    RowMapperBuilder<T> setMapper(ThrowingFunction<T, Row, SQLException> mapper);
}
