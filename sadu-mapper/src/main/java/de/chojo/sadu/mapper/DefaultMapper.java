/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.exceptions.ThrowingBiFunction;
import de.chojo.sadu.types.SqlType;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mapper.util.Results;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class DefaultMapper {
    private DefaultMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static RowMapper<Integer> createInteger(List<SqlType> types) {
        return create(Integer.class, Row::getInt, types);
    }

    public static RowMapper<Long> createLong(List<SqlType> types) {
        return create(Long.class, Row::getLong, types);
    }

    public static RowMapper<Float> createFloat(List<SqlType> types) {
        return create(Float.class, Row::getFloat, types);
    }

    public static RowMapper<Double> createDouble(List<SqlType> types) {
        return create(Double.class, Row::getDouble, types);
    }

    public static RowMapper<String> createString(List<SqlType> types) {
        return create(String.class, Row::getString, types);
    }

    public static RowMapper<Boolean> createBoolean(List<SqlType> types) {
        return create(Boolean.class, Row::getBoolean, types);
    }

    public static RowMapper<Byte[]> createBytes(List<SqlType> types) {
        return create(Byte[].class, (row, columnIndex) -> convertByteArray(row.getBytes(columnIndex)), types);
    }

    public static RowMapper<UUID> createUuid(List<SqlType> textTypes, List<SqlType> byteTypes) {
        return RowMapper.forClass(java.util.UUID.class)
                        .mapper(row -> {
                            var meta = row.getMetaData();
                            var columnIndexOfType = Results.getFirstColumnIndexOfType(meta, textTypes);
                            if (columnIndexOfType.isPresent()) {
                                return row.getUuidFromString(columnIndexOfType.get());
                            }
                            columnIndexOfType = Results.getFirstColumnIndexOfType(meta, byteTypes);
                            var index = columnIndexOfType.orElseThrow(() -> {
                                List<SqlType> sqlTypes = new ArrayList<>(textTypes);
                                sqlTypes.addAll(byteTypes);
                                return createException(sqlTypes, meta);
                            });
                            return row.getUuidFromBytes(index);
                        })
                        .build();
    }

    public static <T> RowMapper<T> create(Class<T> clazz, ThrowingBiFunction<Row, Integer, T, SQLException> mapper, List<SqlType> types) {
        return RowMapper.forClass(clazz)
                        .mapper(row -> {
                            var meta = row.getMetaData();
                            var columnIndexOfType = Results.getFirstColumnIndexOfType(meta, types);
                            var index = columnIndexOfType.orElseThrow(() -> createException(types, meta));
                            return mapper.apply(row, index);
                        }).build();
    }

    private static SQLException createException(List<SqlType> types, ResultSetMetaData meta) {
        var type = types.stream()
                        .map(SqlType::descr)
                        .collect(Collectors.joining(", "));
        var available = "error";
        try {
            available = getColumnTypes(meta).entrySet().stream()
                    .map(e -> "%s %s".formatted(e.getKey(), e.getValue()))
                    .collect(Collectors.joining(", "));
        } catch (SQLException e) {
            // ignore
        }
        return new SQLException("No column of type %s present. Available: %s".formatted(type, available));
    }

    private static Map<String, String> getColumnTypes(ResultSetMetaData meta) throws SQLException {
        Map<String, String> columns = new LinkedHashMap<>();
        for (var i = 1; i <= meta.getColumnCount(); i++) {
            columns.put(
                    "%s | %s".formatted(i, meta.getColumnLabel(i)),
                    "%s (%s)".formatted(meta.getColumnTypeName(i), meta.getColumnType(i)));
        }
        return columns;
    }

    private static Byte[] convertByteArray(byte[] primBytes) {
        var bytes = new Byte[primBytes.length];
        Arrays.setAll(bytes, n -> primBytes[n]);
        return bytes;
    }
}
