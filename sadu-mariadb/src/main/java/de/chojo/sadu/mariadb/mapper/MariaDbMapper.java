/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mariadb.mapper;

import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mariadb.types.MariaDbTypes;

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

public final class MariaDbMapper {
    public static final RowMapper<Boolean> BOOLEAN_MAPPER = createBoolean(List.of(MariaDbTypes.BOOLEAN));
    public static final RowMapper<Short> SHORT_MAPPER = createShort(List.of(MariaDbTypes.TINYINT, MariaDbTypes.SMALLINT));
    public static final RowMapper<Integer> INTEGER_MAPPER = createInteger(List.of(MariaDbTypes.TINYINT, MariaDbTypes.SMALLINT, MariaDbTypes.MEDIUMINT, MariaDbTypes.INT));
    public static final RowMapper<Long> LONG_MAPPER = createLong(List.of(MariaDbTypes.BIGINT, MariaDbTypes.TINYINT, MariaDbTypes.SMALLINT, MariaDbTypes.MEDIUMINT, MariaDbTypes.INT));
    public static final RowMapper<Float> FLOAT_MAPPER = createFloat(List.of(MariaDbTypes.FLOAT));
    public static final RowMapper<Double> DOUBLE_MAPPER = createDouble(List.of(MariaDbTypes.DOUBLE));
    public static final RowMapper<BigDecimal> BIG_DECIMAL_MAPPER = createBigDecimal(List.of(MariaDbTypes.DECIMAL));
    public static final RowMapper<String> STRING_MAPPER = createString(List.of(MariaDbTypes.TINYTEXT, MariaDbTypes.TEXT, MariaDbTypes.MEDIUMTEXT, MariaDbTypes.LONGTEXT, MariaDbTypes.VARCHAR, MariaDbTypes.CHAR));
    public static final RowMapper<Byte[]> BYTES_MAPPER = createBytes(List.of(MariaDbTypes.TINYTEXT, MariaDbTypes.TEXT, MariaDbTypes.MEDIUMTEXT, MariaDbTypes.LONGTEXT, MariaDbTypes.VARCHAR));
    public static final RowMapper<UUID> UUID_MAPPER = createUuid(List.of(MariaDbTypes.TINYTEXT, MariaDbTypes.TEXT, MariaDbTypes.MEDIUMTEXT, MariaDbTypes.LONGTEXT, MariaDbTypes.VARCHAR),
            List.of(MariaDbTypes.TINYBLOB, MariaDbTypes.BLOB, MariaDbTypes.MEDIUMBLOB, MariaDbTypes.LONGBLOB, MariaDbTypes.VARBINARY));
    private MariaDbMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static List<RowMapper<?>> getDefaultMapper() {
        return List.of(BOOLEAN_MAPPER, SHORT_MAPPER, INTEGER_MAPPER, LONG_MAPPER, FLOAT_MAPPER, DOUBLE_MAPPER, BIG_DECIMAL_MAPPER, STRING_MAPPER, BYTES_MAPPER, UUID_MAPPER);
    }
}
