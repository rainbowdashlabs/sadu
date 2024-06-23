/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.jackson.exception.UnknownTypeException;
import de.chojo.sadu.mapper.IRowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.IRowMapper;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JacksonRowMapperFactory {
    private final IRowMapperRegistry registry;
    private final ObjectMapper objectMapper;
    private final Map<Class<?>, IRowMapper<?>> mapper = new HashMap<>();

    public JacksonRowMapperFactory(ObjectMapper objectMapper, IRowMapperRegistry registry) {
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public <T, V extends IRowMapper<T>> V forClass(Class<T> clazz) {
        return (V) mapper.computeIfAbsent(clazz, key -> new JacksonRowMapper<>(key, objectMapper, registry));
    }
}
