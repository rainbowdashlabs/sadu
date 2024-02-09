/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.call;

import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.call.CallImpl;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.core.types.SqlType;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collection;

@SuppressWarnings("unused")
public sealed interface Call permits CallImpl {
    /**
     * Creates a new instance of the Call class.
     *
     * @return A new instance of the Call class.
     */
    static Call of() {
        return new CallImpl();
    }

    /**
     * Creates a new instance of the Call class.
     *
     * @return A new instance of the Call class.
     */
    static Call call() {
        return of();
    }

    Call bind(String value);

    Call bind(String token, String value);

    Call bind(Enum<?> value);

    Call bind(String token, Enum<?> value);

    Call bind(short value);

    Call bind(String token, short value);

    Call bind(int value);

    Call bind(long value);

    Call bind(String token, long value);

    Call bind(String token, int value);

    Call bind(float value);

    Call bind(String token, float value);

    Call bind(double value);

    Call bind(String token, double value);

    Call bind(BigDecimal value);

    Call bind(String token, BigDecimal value);

    Call bind(boolean value);

    Call bind(String token, boolean value);

    Call bind(byte value);

    Call bind(String token, byte value);

    Call bind(byte[] value);

    Call bind(String token, byte[] value);

    Call bind(Date value);

    Call bind(String token, Date value);

    Call bind(LocalDate value);

    Call bind(String token, LocalDate value);

    Call bind(Time value);

    Call bind(String token, Time value);

    Call bind(LocalTime value);

    Call bind(String token, LocalTime value);

    Call bind(Timestamp value);

    Call bind(String token, Timestamp value);

    Call bind(LocalDateTime value);

    Call bind(String token, LocalDateTime value);

    Call bind(ZonedDateTime value);

    Call bind(String token, ZonedDateTime value);

    Call bind(OffsetDateTime value);

    Call bind(String token, OffsetDateTime value);

    Call bind(OffsetTime value);

    Call bind(String token, OffsetTime value);

    Call bind(Ref value);

    Call bind(String token, Ref value);

    Call bind(Blob value);

    Call bind(String token, Blob value);

    Call bind(Clob value);

    Call bind(String token, Clob value);

    Call bind(URL value);

    Call bind(String token, URL value);

    Call bind(RowId value);

    Call bind(String token, RowId value);

    Call bind(Collection<?> value, SqlType type);

    Call bind(String token, Collection<?> value, SqlType type);

    Call bind(Object[] value, SqlType type);

    Call bind(String token, Object[] value, SqlType type);

    <T> Call bind(String token, T value, Adapter<T> adapter);

    <T> Call bind(T value, Adapter<T> adapter);

    SingletonCall asSingleCall();

    BatchCall asBatchCall();
}
