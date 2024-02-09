/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.parameter;

import de.chojo.sadu.core.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.api.parameter.BaseParameter;
import de.chojo.sadu.queries.query.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IndexParameter implements BaseParameter {
    private final int index;
    private final ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply;

    public IndexParameter(int index, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        this.index = index;
        this.apply = apply;
    }

    @Override
    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        apply.accept(stmt, query.getIndexTokenIndex(index));
    }
}
