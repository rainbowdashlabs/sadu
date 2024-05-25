/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetMapper {
    private final RowMapperRegistry registry;
    private final ObjectMapper objectMapper;

    public ResultSetMapper(ObjectMapper objectMapper, RowMapperRegistry registry) {
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    public <T> T convertRow(Row row, Class<T> clazz) throws SQLException {
        ResultSetMetaData meta = row.getMetaData();
        Map<String, Object> map = new HashMap<>();
        for (int index = 1; index < meta.getColumnCount() + 1; index++) {
            map.put(meta.getColumnName(index), row.getObject(index));
//            var type = meta.getColumnTypeName(index);
//            Optional<RowMapper<?>> forType = registry.findForType(type);
//            var mapper = forType.orElseThrow(() -> new UnknownTypeException(type));
//            map.put(meta.getColumnName(index), mapper.map(row, index));
        }

        return objectMapper.convertValue(map, clazz);
    }
}
