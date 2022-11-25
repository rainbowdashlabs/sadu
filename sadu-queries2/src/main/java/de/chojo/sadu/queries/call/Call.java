/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call;

import de.chojo.sadu.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.adapter.Adapter;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.params.BaseParam;
import de.chojo.sadu.queries.params.IndexParam;
import de.chojo.sadu.queries.params.TokenParam;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * A call is a subelement of a {@link Calls}. It represents a single query call of any kind.
 */
public class Call {
    private int index = 1;
    private final List<BaseParam> tokens = new ArrayList<>();

    public static Call of() {
        return new Call();
    }

    private int nextIndex() {
        return index++;
    }

    private Call addToken(String token, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        tokens.add(new TokenParam(token, apply));
        return this;
    }

    private Call addToken(ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        tokens.add(new IndexParam(nextIndex(), apply));
        return this;
    }

    private ThrowingBiConsumer<PreparedStatement, Integer, SQLException> nullSave(Object value, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply, int type) {
        if (value == null) {
            return (stmt, index) -> stmt.setNull(index, type);
        }
        return apply;
    }

    public Call bind(String value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setString(index, value), Types.VARCHAR));
    }

    public Call bind(String token, String value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setString(index, value), Types.VARCHAR));
    }

    public Call bind(int value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setInt(index, value), Types.INTEGER));
    }

    public Call bind(String token, int value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setInt(index, value), Types.INTEGER));
    }

    public Call bind(boolean value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setBoolean(index, value), Types.BOOLEAN));
    }

    public Call bind(String token, boolean value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setBoolean(index, value), Types.BOOLEAN));
    }

    public Call bind(long value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setLong(index, value), Types.BIGINT));
    }

    public Call bind(String token, long value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setLong(index, value), Types.BIGINT));
    }

    public Call bind(double value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setDouble(index, value), Types.BIGINT));
    }

    public Call bind(String token, double value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setDouble(index, value), Types.BIGINT));
    }
    public Call bind(float value) {
        return addToken(nullSave(value, (stmt, index) -> stmt.setFloat(index, value), Types.BIGINT));
    }

    public Call bind(String token, float value) {
        return addToken(token, nullSave(value, (stmt, index) -> stmt.setFloat(index, value), Types.BIGINT));
    }

    public Call bind(Adapter value) {
        return addToken(nullSave(value.object(), value.apply(), value.type()));
    }

    public Call bind(String token, Adapter value) {
        return addToken(token, nullSave(value.object(), value.apply(), value.type()));
    }

    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        for (var token : tokens) {
            token.apply(query, stmt);
        }
    }
}
