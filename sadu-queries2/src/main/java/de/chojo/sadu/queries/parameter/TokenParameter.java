/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.parameter;

import de.chojo.sadu.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.api.parameter.BaseParameter;
import de.chojo.sadu.queries.query.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TokenParameter implements BaseParameter {
    private final String token;
    private final ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply;

    public TokenParameter(String token, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
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
