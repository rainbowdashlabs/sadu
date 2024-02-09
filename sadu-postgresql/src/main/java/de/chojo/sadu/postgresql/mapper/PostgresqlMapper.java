/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.postgresql.mapper;

import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.postgresql.types.PostgreSqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static de.chojo.sadu.mapper.DefaultMapper.createBigDecimal;
import static de.chojo.sadu.mapper.DefaultMapper.createBoolean;
import static de.chojo.sadu.mapper.DefaultMapper.createBytes;
import static de.chojo.sadu.mapper.DefaultMapper.createDouble;
import static de.chojo.sadu.mapper.DefaultMapper.createFloat;
import static de.chojo.sadu.mapper.DefaultMapper.createInteger;
import static de.chojo.sadu.mapper.DefaultMapper.createLong;
import static de.chojo.sadu.mapper.DefaultMapper.createShort;
import static de.chojo.sadu.mapper.DefaultMapper.createString;
import static de.chojo.sadu.mapper.DefaultMapper.createUuid;

public final class PostgresqlMapper {
    public static final RowMapper<Boolean> BOOLEAN_MAPPER = createBoolean(List.of(PostgreSqlTypes.BOOLEAN));
    public static final RowMapper<Short> SHORT_MAPPER = createShort(List.of(PostgreSqlTypes.SMALLINT, PostgreSqlTypes.INTEGER));
    public static final RowMapper<Integer> INTEGER_MAPPER = createInteger(List.of(PostgreSqlTypes.SMALLINT, PostgreSqlTypes.INTEGER));
    public static final RowMapper<Long> LONG_MAPPER = createLong(List.of(PostgreSqlTypes.SMALLINT, PostgreSqlTypes.INTEGER, PostgreSqlTypes.BIGINT));
    public static final RowMapper<Float> FLOAT_MAPPER = createFloat(List.of(PostgreSqlTypes.REAL));
    public static final RowMapper<Double> DOUBLE_MAPPER = createDouble(List.of(PostgreSqlTypes.DECIMAL, PostgreSqlTypes.NUMERIC, PostgreSqlTypes.DOUBLE, PostgreSqlTypes.REAL));
    public static final RowMapper<BigDecimal> BIG_DECIMAL_MAPPER = createBigDecimal(List.of(PostgreSqlTypes.DECIMAL, PostgreSqlTypes.NUMERIC, PostgreSqlTypes.DOUBLE, PostgreSqlTypes.REAL));
    public static final RowMapper<String> STRING_MAPPER = createString(List.of(PostgreSqlTypes.TEXT, PostgreSqlTypes.VARCHAR, PostgreSqlTypes.CHAR, PostgreSqlTypes.JSON, PostgreSqlTypes.JSONB));
    public static final RowMapper<Byte[]> BYTES_MAPPER = createBytes(List.of(PostgreSqlTypes.BYTEA));
    public static final RowMapper<UUID> UUID_MAPPER = createUuid(List.of(PostgreSqlTypes.TEXT), List.of(PostgreSqlTypes.BYTEA));
    private PostgresqlMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static List<RowMapper<?>> getDefaultMapper() {
        return List.of(BOOLEAN_MAPPER, SHORT_MAPPER, INTEGER_MAPPER, LONG_MAPPER, FLOAT_MAPPER, DOUBLE_MAPPER, BIG_DECIMAL_MAPPER, STRING_MAPPER, BYTES_MAPPER, UUID_MAPPER);
    }
}
