/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter;

import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.queries.api.call.adapter.Adapter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import static de.chojo.sadu.core.conversion.ArrayConverter.toSqlArray;

/**
 * The StandardAdapter class provides a set of static instances of Adapter, which map Java objects to specific SQL data types and provide the necessary conversions when binding the
 * objects to a PreparedStatement.
 */
public final class StandardAdapter {
    public static final Adapter<String> STRING = Adapter.create(PreparedStatement::setString, Types.VARCHAR);
    public static final Adapter<Enum<?>> ENUM = Adapter.create(PreparedStatement::setString, Types.VARCHAR, Enum::name);
    public static final Adapter<Short> SHORT = Adapter.create(PreparedStatement::setShort, Types.TINYINT);
    public static final Adapter<Integer> INTEGER = Adapter.create(PreparedStatement::setInt, Types.INTEGER);
    public static final Adapter<BigDecimal> BIG_DECIMAL = Adapter.create(PreparedStatement::setBigDecimal, Types.DECIMAL);
    public static final Adapter<Float> FLOAT = Adapter.create(PreparedStatement::setFloat, Types.FLOAT);
    public static final Adapter<Double> DOUBLE = Adapter.create(PreparedStatement::setDouble, Types.DOUBLE);
    public static final Adapter<Long> LONG = Adapter.create(PreparedStatement::setLong, Types.BIGINT);
    public static final Adapter<Boolean> BOOLEAN = Adapter.create(PreparedStatement::setBoolean, Types.BOOLEAN);
    public static final Adapter<Byte> BYTE = Adapter.create(PreparedStatement::setByte, Types.BIT);
    public static final Adapter<byte[]> BYTE_ARRAY = Adapter.create(PreparedStatement::setBytes, Types.BINARY);
    public static final Adapter<Date> DATE = Adapter.create(PreparedStatement::setDate, Types.DATE);
    public static final Adapter<LocalDate> LOCAL_DATE = Adapter.create(PreparedStatement::setDate, Types.DATE, Date::valueOf);
    public static final Adapter<Time> TIME = Adapter.create(PreparedStatement::setTime, Types.TIME);
    public static final Adapter<LocalTime> LOCAL_TIME = Adapter.create(PreparedStatement::setTime, Types.TIME, Time::valueOf);
    public static final Adapter<Timestamp> TIMESTAMP = Adapter.create(PreparedStatement::setTimestamp, Types.TIMESTAMP);
    public static final Adapter<LocalDateTime> LOCAL_DATE_TIME = Adapter.create(PreparedStatement::setTimestamp, Types.TIMESTAMP, Timestamp::valueOf);
    public static final Adapter<ZonedDateTime> ZONED_DATE_TIME = Adapter.create(PreparedStatement::setTimestamp, Types.TIMESTAMP, v -> Timestamp.valueOf(v.toLocalDateTime()));
    public static final Adapter<OffsetDateTime> OFFSET_DATE_TIME = Adapter.create(PreparedStatement::setTimestamp, Types.TIMESTAMP, v -> Timestamp.valueOf(v.toLocalDateTime()));
    public static final Adapter<OffsetTime> OFFSET_TIME = Adapter.create(PreparedStatement::setTime, Types.TIME, v -> Time.valueOf(v.toLocalTime()));
    public static final Adapter<Ref> REF = Adapter.create(PreparedStatement::setRef, Types.REF);
    public static final Adapter<Blob> BLOB = Adapter.create(PreparedStatement::setBlob, Types.BLOB);
    public static final Adapter<Clob> CLOB = Adapter.create(PreparedStatement::setClob, Types.CLOB);
    public static final Adapter<URL> URL = Adapter.create(PreparedStatement::setURL, Types.DATALINK);
    public static final Adapter<RowId> ROW_ID = Adapter.create(PreparedStatement::setRowId, Types.ROWID);

    private StandardAdapter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static Adapter<Collection<?>> forCollection(Collection<?> list, SqlType type) {
        return Adapter.create((stmt, index, value) -> stmt.setArray(index, toSqlArray(stmt.getConnection(), type, list)), Types.ARRAY);
    }

    public static Adapter<Object[]> forArray(Object[] array, SqlType type) {
        return Adapter.create((stmt, index, value) -> stmt.setArray(index, toSqlArray(stmt.getConnection(), type, array)), Types.ARRAY);
    }
}
