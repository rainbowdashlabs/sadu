/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.params;

import de.chojo.sadu.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IndexParam implements BaseParam {
    private final int index;
    private final ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply;

    public IndexParam(int index, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        this.index = index;
        this.apply = apply;
    }

    @Override
    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        apply.accept(stmt, query.getIndexTokenIndex(index));
    }
}
