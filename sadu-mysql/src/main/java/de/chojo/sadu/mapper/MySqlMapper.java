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
import static de.chojo.sadu.types.MySqlTypes.BIGINT;
import static de.chojo.sadu.types.MySqlTypes.BLOB;
import static de.chojo.sadu.types.MySqlTypes.BOOLEAN;
import static de.chojo.sadu.types.MySqlTypes.DECIMAL;
import static de.chojo.sadu.types.MySqlTypes.DOUBLE;
import static de.chojo.sadu.types.MySqlTypes.FLOAT;
import static de.chojo.sadu.types.MySqlTypes.INT;
import static de.chojo.sadu.types.MySqlTypes.LONGBLOB;
import static de.chojo.sadu.types.MySqlTypes.LONGTEXT;
import static de.chojo.sadu.types.MySqlTypes.MEDIUMBLOB;
import static de.chojo.sadu.types.MySqlTypes.MEDIUMINT;
import static de.chojo.sadu.types.MySqlTypes.MEDIUMTEXT;
import static de.chojo.sadu.types.MySqlTypes.SMALLINT;
import static de.chojo.sadu.types.MySqlTypes.TEXT;
import static de.chojo.sadu.types.MySqlTypes.TINYBLOB;
import static de.chojo.sadu.types.MySqlTypes.TINYINT;
import static de.chojo.sadu.types.MySqlTypes.TINYTEXT;
import static de.chojo.sadu.types.MySqlTypes.VARBINARY;
import static de.chojo.sadu.types.MySqlTypes.VARCHAR;

public final class MySqlMapper {
    private MySqlMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static final RowMapper<Boolean> BOOLEAN_MAPPER = createBoolean(List.of(BOOLEAN));
    public static final RowMapper<Integer> INTEGER_MAPPER = createInteger(List.of(TINYINT, SMALLINT, MEDIUMINT, INT));
    public static final RowMapper<Long> LONG_MAPPER = createLong(List.of(BIGINT, TINYINT, SMALLINT, MEDIUMINT, INT));
    public static final RowMapper<Float> FLOAT_MAPPER = createFloat(List.of(DOUBLE, DECIMAL, FLOAT));
    public static final RowMapper<Double> DOUBLE_MAPPER = createDouble(List.of(DOUBLE, DECIMAL, FLOAT));
    public static final RowMapper<String> STRING_MAPPER = createString(List.of(TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, VARCHAR));
    public static final RowMapper<Byte[]> BYTES_MAPPER = createBytes(List.of(TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, VARCHAR));
    public static final RowMapper<UUID> UUID_MAPPER = createUuid(List.of(TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, VARCHAR),
            List.of(TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB, VARBINARY));

    public static List<RowMapper<?>> getDefaultMapper(){
        return List.of(BOOLEAN_MAPPER, INTEGER_MAPPER, LONG_MAPPER, FLOAT_MAPPER, DOUBLE_MAPPER, STRING_MAPPER, BYTES_MAPPER, UUID_MAPPER);
    }
}
