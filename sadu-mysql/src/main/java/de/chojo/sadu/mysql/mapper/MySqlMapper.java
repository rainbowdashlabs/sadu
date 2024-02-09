/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mysql.mapper;

import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mysql.types.MySqlTypes;

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

public final class MySqlMapper {
    public static final RowMapper<Boolean> BOOLEAN_MAPPER = createBoolean(List.of(MySqlTypes.BOOLEAN));
    public static final RowMapper<Integer> INTEGER_MAPPER = createInteger(List.of(MySqlTypes.TINYINT, MySqlTypes.SMALLINT, MySqlTypes.MEDIUMINT, MySqlTypes.INT));
    public static final RowMapper<Long> LONG_MAPPER = createLong(List.of(MySqlTypes.BIGINT, MySqlTypes.TINYINT, MySqlTypes.SMALLINT, MySqlTypes.MEDIUMINT, MySqlTypes.INT));
    public static final RowMapper<Float> FLOAT_MAPPER = createFloat(List.of(MySqlTypes.DOUBLE, MySqlTypes.DECIMAL, MySqlTypes.FLOAT));
    public static final RowMapper<Double> DOUBLE_MAPPER = createDouble(List.of(MySqlTypes.DOUBLE, MySqlTypes.DECIMAL, MySqlTypes.FLOAT));
    public static final RowMapper<String> STRING_MAPPER = createString(List.of(MySqlTypes.TINYTEXT, MySqlTypes.TEXT, MySqlTypes.MEDIUMTEXT, MySqlTypes.LONGTEXT, MySqlTypes.VARCHAR));
    public static final RowMapper<Byte[]> BYTES_MAPPER = createBytes(List.of(MySqlTypes.TINYTEXT, MySqlTypes.TEXT, MySqlTypes.MEDIUMTEXT, MySqlTypes.LONGTEXT, MySqlTypes.VARCHAR));
    public static final RowMapper<UUID> UUID_MAPPER = createUuid(List.of(MySqlTypes.TINYTEXT, MySqlTypes.TEXT, MySqlTypes.MEDIUMTEXT, MySqlTypes.LONGTEXT, MySqlTypes.VARCHAR),
            List.of(MySqlTypes.TINYBLOB, MySqlTypes.BLOB, MySqlTypes.MEDIUMBLOB, MySqlTypes.LONGBLOB, MySqlTypes.VARBINARY));
    private MySqlMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static List<RowMapper<?>> getDefaultMapper() {
        return List.of(BOOLEAN_MAPPER, INTEGER_MAPPER, LONG_MAPPER, FLOAT_MAPPER, DOUBLE_MAPPER, STRING_MAPPER, BYTES_MAPPER, UUID_MAPPER);
    }
}
