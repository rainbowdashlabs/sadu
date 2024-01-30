/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call;

import de.chojo.sadu.exceptions.ThrowingBiConsumer;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.api.call.calls.Calls;
import de.chojo.sadu.queries.api.parameter.BaseParameter;
import de.chojo.sadu.queries.call.adapter.StandardAdapter;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.parameter.IndexParameter;
import de.chojo.sadu.queries.parameter.TokenParameter;
import de.chojo.sadu.queries.query.TokenizedQuery;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static de.chojo.sadu.queries.call.adapter.StandardAdapter.BIG_DECIMAL;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.BLOB;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.BOOLEAN;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.BYTE;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.BYTE_ARRAY;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.CLOB;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.DATE;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.DOUBLE;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.ENUM;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.FLOAT;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.INTEGER;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.LOCAL_DATE;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.LOCAL_DATE_TIME;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.LOCAL_TIME;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.LONG;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.OFFSET_DATE_TIME;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.OFFSET_TIME;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.REF;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.ROW_ID;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.SHORT;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.STRING;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.TIME;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.TIMESTAMP;
import static de.chojo.sadu.queries.call.adapter.StandardAdapter.ZONED_DATE_TIME;

/**
 * A call is a subelement of a {@link Calls}. It represents a single query call of any kind.
 */
public final class CallImpl implements Call {
    private final List<BaseParameter> tokens = new ArrayList<>();
    private int index = 1;

    public CallImpl() {
    }

    private int nextIndex() {
        return index++;
    }

    private Call addToken(String token, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        tokens.add(new TokenParameter(token, apply));
        return this;
    }

    private Call addToken(ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply) {
        tokens.add(new IndexParameter(nextIndex(), apply));
        return this;
    }

    private ThrowingBiConsumer<PreparedStatement, Integer, SQLException> nullSave(Object value, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply, int type) {
        if (value == null) return (stmt, index) -> stmt.setNull(index, type);
        return apply;
    }

    @Override
    public Call bind(String value) {
        return bind(value, STRING);
    }

    @Override
    public Call bind(String token, String value) {
        return bind(token, value, STRING);
    }

    @Override
    public Call bind(Enum<?> value) {
        return bind(value, ENUM);
    }

    @Override
    public Call bind(String token, Enum<?> value) {
        return bind(token, value, ENUM);
    }

    @Override
    public Call bind(short value) {
        return bind(value, SHORT);
    }

    @Override
    public Call bind(String token, short value) {
        return bind(token, value, SHORT);
    }

    @Override
    public Call bind(int value) {
        return bind(value, INTEGER);
    }

    @Override
    public Call bind(String token, int value) {
        return bind(token, value, INTEGER);
    }

    @Override
    public Call bind(boolean value) {
        return bind(value, BOOLEAN);
    }

    @Override
    public Call bind(String token, boolean value) {
        return bind(token, value, BOOLEAN);
    }

    @Override
    public Call bind(byte value) {
        return bind(value, BYTE);
    }

    @Override
    public Call bind(String token, byte value) {
        return bind(token, value, BYTE);
    }

    @Override
    public Call bind(byte[] value) {
        return bind(value, BYTE_ARRAY);
    }

    @Override
    public Call bind(String token, byte[] value) {
        return bind(token, value, BYTE_ARRAY);
    }

    @Override
    public Call bind(Date value) {
        return bind(value, DATE);
    }

    @Override
    public Call bind(String token, Date value) {
        return bind(token, value, DATE);
    }

    @Override
    public Call bind(long value) {
        return bind(value, LONG);
    }

    @Override
    public Call bind(String token, long value) {
        return bind(token, value, LONG);
    }

    @Override
    public Call bind(double value) {
        return bind(value, DOUBLE);
    }

    @Override
    public Call bind(String token, double value) {
        return bind(token, value, DOUBLE);
    }

    @Override
    public Call bind(BigDecimal value) {
        return bind(value, BIG_DECIMAL);
    }

    @Override
    public Call bind(String token, BigDecimal value) {
        return bind(token, value, BIG_DECIMAL);
    }

    @Override
    public Call bind(float value) {
        return bind(value, FLOAT);
    }

    @Override
    public Call bind(String token, float value) {
        return bind(token, value, FLOAT);
    }

    @Override
    public Call bind(LocalDate value) {
        return bind(value, LOCAL_DATE);
    }

    @Override
    public Call bind(String token, LocalDate value) {
        return bind(token, value, LOCAL_DATE);
    }

    @Override
    public Call bind(Time value) {
        return bind(value, TIME);
    }

    @Override
    public Call bind(String token, Time value) {
        return bind(token, value, TIME);
    }

    @Override
    public Call bind(LocalTime value) {
        return bind(value, LOCAL_TIME);
    }

    @Override
    public Call bind(String token, LocalTime value) {
        return bind(token, value, LOCAL_TIME);
    }

    @Override
    public Call bind(Timestamp value) {
        return bind(value, TIMESTAMP);
    }

    @Override
    public Call bind(String token, Timestamp value) {
        return bind(token, value, TIMESTAMP);
    }

    @Override
    public Call bind(LocalDateTime value) {
        return bind(value, LOCAL_DATE_TIME);
    }

    @Override
    public Call bind(String token, LocalDateTime value) {
        return bind(token, value, LOCAL_DATE_TIME);
    }

    @Override
    public Call bind(ZonedDateTime value) {
        return bind(value, ZONED_DATE_TIME);
    }

    @Override
    public Call bind(String token, ZonedDateTime value) {
        return bind(token, value, ZONED_DATE_TIME);
    }

    @Override
    public Call bind(OffsetDateTime value) {
        return bind(value, OFFSET_DATE_TIME);
    }

    @Override
    public Call bind(String token, OffsetDateTime value) {
        return bind(token, value, OFFSET_DATE_TIME);
    }

    @Override
    public Call bind(OffsetTime value) {
        return bind(value, OFFSET_TIME);
    }

    @Override
    public Call bind(String token, OffsetTime value) {
        return bind(token, value, OFFSET_TIME);
    }

    @Override
    public Call bind(Ref value) {
        return bind(value, REF);
    }

    @Override
    public Call bind(String token, Ref value) {
        return bind(token, value, REF);
    }

    @Override
    public Call bind(Blob value) {
        return bind(value, BLOB);
    }

    @Override
    public Call bind(String token, Blob value) {
        return bind(token, value, BLOB);
    }

    @Override
    public Call bind(Clob value) {
        return bind(value, CLOB);
    }

    @Override
    public Call bind(String token, Clob value) {
        return bind(token, value, CLOB);
    }

    @Override
    public Call bind(URL value) {
        return bind(value, StandardAdapter.URL);
    }

    @Override
    public Call bind(String token, URL value) {
        return bind(token, value, StandardAdapter.URL);
    }

    @Override
    public Call bind(RowId value) {
        return bind(value, ROW_ID);
    }

    @Override
    public Call bind(String token, RowId value) {
        return bind(token, value, ROW_ID);
    }

    @Override
    public <T> Call bind(String token, T value, Adapter<T> adapter) {
        return addToken(token, nullSave(value, (stmt, index) -> adapter.apply(stmt, index, value), adapter.type()));
    }

    @Override
    public <T> Call bind(T value, Adapter<T> adapter) {
        return addToken(nullSave(value, (stmt, index) -> adapter.apply(stmt, index, value), adapter.type()));
    }

    public void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException {
        for (var token : tokens) {
            token.apply(query, stmt);
        }
    }

    /**
     * Returns a SingletonCall object for the current Call instance.
     * This allows executing the query with a single set of arguments.
     *
     * @return A SingletonCall object
     */
    @Override
    public SingletonCall asSingleCall() {
        return new SingletonCall(this);
    }

    /**
     * Converts the current Call object into a BatchCall object.
     *
     * @return a new instance of the BatchCall class with the current Call object added to it.
     */
    @Override
    public BatchCall asBatchCall() {
        return new BatchCall(List.of(this));
    }
}
