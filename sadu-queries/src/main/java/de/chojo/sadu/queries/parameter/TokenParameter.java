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

import static de.chojo.sadu.queries.query.TokenizedQuery.ALLOWED_TOKEN_CHARACTER;
import static de.chojo.sadu.queries.query.TokenizedQuery.TOKEN_PATTERN;

public class TokenParameter implements BaseParameter {
    private final String token;
    private final ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply;

    public TokenParameter(String token, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        if (!token.startsWith(":")) {
            this.token = ":" + token;
        } else {
            this.token = token;
        }
        if (!TOKEN_PATTERN.matcher(this.token).matches()) {
            throw new IllegalArgumentException("Illegal token \"" + this.token.substring(1) + "\". Tokens may only contain characters which match the expression: \"" + ALLOWED_TOKEN_CHARACTER + "\"");
        }
        this.apply = apply;
    }

    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        for (var index : query.getNamedTokenIndex(token)) {
            apply.accept(stmt, index);
        }
    }
}
