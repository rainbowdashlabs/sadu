/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.params;

import de.chojo.sadu.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TokenParam implements BaseParam {
    private final String token;
    private final ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply;

    public TokenParam(String token, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        if (!token.startsWith(":")) {
            this.token = ":" + token;
        } else {
            this.token = token;
        }
        this.apply = apply;
    }

    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        for (var index : query.getNamedTokenIndex(token)) {
            apply.accept(stmt, index);
        }
    }
}
