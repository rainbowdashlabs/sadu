/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.core.types.SqlType;
import de.chojo.sadu.jackson.exception.UnknownTypeException;
import de.chojo.sadu.mapper.IRowMapperRegistry;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.IRowMapper;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonRowMapper<T> implements IRowMapper<T> {
    private final Class<T> clazz;
    private final ObjectMapper objectMapper;
    private final IRowMapperRegistry registry;

    public JacksonRowMapper(Class<T> clazz, ObjectMapper objectMapper, IRowMapperRegistry registry) {
        this.clazz = clazz;
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    @Override
    public T map(Row row) throws SQLException {
        ResultSetMetaData meta = row.getMetaData();
        Map<String, Object> map = new HashMap<>();
        for (int index = 1; index < meta.getColumnCount() + 1; index++) {
            // Maybe this works already
            //map.put(meta.getColumnName(index), row.getObject(index));
            var type = meta.getColumnTypeName(index);
            var forType = registry.findForType(type);
            var mapper = forType.orElseThrow(() -> new UnknownTypeException(type));
            Object mapped = mapper.map(row, index);
            if (mapped instanceof Timestamp timestamp) {
                mapped = DateTimeFormatter.ISO_INSTANT.format(timestamp.toInstant());
            }
            map.put(meta.getColumnName(index), mapped);
        }

        return objectMapper.convertValue(map, clazz);
    }

    @Override
    public T map(Row row, int index) throws SQLException {
        throw new UnsupportedOperationException("A Jackson row Mapper can not map by index.");
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public int applicable(ResultSet resultSet) throws SQLException {
        return applicable(resultSet.getMetaData());
    }

    @Override
    public int applicable(ResultSet resultSet, MapperConfig config) throws SQLException {
        return applicable(resultSet.getMetaData(), config);
    }

    @Override
    public int applicable(ResultSetMetaData meta) throws SQLException {
        return meta.getColumnCount();
    }

    @Override
    public int applicable(ResultSetMetaData meta, MapperConfig config) throws SQLException {
        return applicable(meta);
    }

    @Override
    public List<SqlType> types() {
        return List.of();
    }
}
