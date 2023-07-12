/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.mapper.rowmapper.RowMapper;

import java.util.List;
import java.util.UUID;

import static de.chojo.sadu.mapper.DefaultMapper.createBoolean;
import static de.chojo.sadu.mapper.DefaultMapper.createBytes;
import static de.chojo.sadu.mapper.DefaultMapper.createDouble;
import static de.chojo.sadu.mapper.DefaultMapper.createFloat;
import static de.chojo.sadu.mapper.DefaultMapper.createInteger;
import static de.chojo.sadu.mapper.DefaultMapper.createLong;
import static de.chojo.sadu.mapper.DefaultMapper.createString;
import static de.chojo.sadu.mapper.DefaultMapper.createUuid;
import static de.chojo.sadu.types.PostgreSqlTypes.BIGINT;
import static de.chojo.sadu.types.PostgreSqlTypes.BOOLEAN;
import static de.chojo.sadu.types.PostgreSqlTypes.BYTEA;
import static de.chojo.sadu.types.PostgreSqlTypes.CHAR;
import static de.chojo.sadu.types.PostgreSqlTypes.DECIMAL;
import static de.chojo.sadu.types.PostgreSqlTypes.DOUBLE;
import static de.chojo.sadu.types.PostgreSqlTypes.INTEGER;
import static de.chojo.sadu.types.PostgreSqlTypes.NUMERIC;
import static de.chojo.sadu.types.PostgreSqlTypes.SMALLINT;
import static de.chojo.sadu.types.PostgreSqlTypes.TEXT;
import static de.chojo.sadu.types.PostgreSqlTypes.VARCHAR;

public final class PostgresqlMapper {
    private PostgresqlMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static final RowMapper<Boolean> BOOLEAN_MAPPER = createBoolean(List.of(BOOLEAN));
    public static final RowMapper<Integer> INTEGER_MAPPER = createInteger(List.of(SMALLINT, INTEGER));
    public static final RowMapper<Long> LONG_MAPPER = createLong(List.of(SMALLINT, INTEGER, BIGINT));
    public static final RowMapper<Float> FLOAT_MAPPER = createFloat(List.of(DECIMAL, NUMERIC, DOUBLE));
    public static final RowMapper<Double> DOUBLE_MAPPER = createDouble(List.of(DECIMAL, NUMERIC, DOUBLE));
    public static final RowMapper<String> STRING_MAPPER = createString(List.of(TEXT, VARCHAR, CHAR));
    public static final RowMapper<Byte[]> BYTES_MAPPER = createBytes(List.of(BYTEA));
    public static final RowMapper<UUID> UUID_MAPPER = createUuid(List.of(TEXT), List.of(BYTEA));

    public static List<RowMapper<?>> getDefaultMapper() {
        return List.of(BOOLEAN_MAPPER, INTEGER_MAPPER, LONG_MAPPER, FLOAT_MAPPER, DOUBLE_MAPPER, STRING_MAPPER, BYTES_MAPPER, UUID_MAPPER);
    }
}
