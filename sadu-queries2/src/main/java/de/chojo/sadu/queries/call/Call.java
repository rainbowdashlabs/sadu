/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.BOOLEAN;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.DOUBLE;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.FLOAT;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.INTEGER;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.LOCAL_DATE;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.LONG;
import static de.chojo.sadu.queries.call.adapter.impl.StandardAdapter.STRING;

/**
 * A call is a subelement of a {@link Calls}. It represents a single query call of any kind.
 */
public class Call {
    private final List<BaseParam> tokens = new ArrayList<>();
    private int index = 1;

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
        if (value == null) return (stmt, index) -> stmt.setNull(index, type);
        return apply;
    }

    public Call bind(String value) {
        return bind(value, STRING);
    }

    public Call bind(String token, String value) {
        return bind(token, value, STRING);
    }

    public Call bind(int value) {
        return bind(value, INTEGER);
    }

    public Call bind(String token, int value) {
        return bind(token, value, INTEGER);
    }

    public Call bind(boolean value) {
        return bind(value, BOOLEAN);
    }

    public Call bind(String token, boolean value) {
        return bind(token, value, BOOLEAN);
    }

    public Call bind(long value) {
        return bind(value, LONG);
    }

    public Call bind(String token, long value) {
        return bind(token, value, LONG);
    }

    public Call bind(double value) {
        return bind(value, DOUBLE);
    }

    public Call bind(String token, double value) {
        return bind(token, value, DOUBLE);
    }

    public Call bind(float value) {
        return bind(value, FLOAT);
    }

    public Call bind(String token, float value) {
        return bind(token, value, FLOAT);
    }

    public Call bind(LocalDate value) {
        return bind(value, LOCAL_DATE);
    }

    public Call bind(String token, LocalDate value) {
        return bind(token, value, LOCAL_DATE);
    }

    public <T> Call bind(String token, T value, Adapter<T> adapter) {
        return addToken(token, nullSave(value, (stmt, index) -> adapter.apply(stmt, index, value), adapter.type()));
    }

    public <T> Call bind(T value, Adapter<T> adapter) {
        return addToken(nullSave(value, (stmt, index) -> adapter.apply(stmt, index, value), adapter.type()));
    }

    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        for (var token : tokens) {
            token.apply(query, stmt);
        }
    }
}
