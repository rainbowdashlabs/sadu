/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.chojo.sadu.mapper.IRowMapperRegistry;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.exceptions.MappingException;
import de.chojo.sadu.mapper.rowmapper.IRowMapper;
import de.chojo.sadu.mapper.rowmapper.RowMapper;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JacksonRowMapperRegistry implements IRowMapperRegistry {
    private final JacksonRowMapperFactory factory;
    private final IRowMapperRegistry registry;

    public JacksonRowMapperRegistry(ObjectMapper objectMapper, IRowMapperRegistry registry) {
        this.registry = registry;
        this.factory = new JacksonRowMapperFactory(objectMapper, registry);
    }

    @Override
    public List<RowMapper<?>> mapper(Class<?> clazz) {
        return List.of();
    }

    @Override
    public <T> Optional<IRowMapper<T>> wildcard(Class<T> clazz) {
        return Optional.ofNullable(registry.wildcard(clazz).orElseGet(() -> factory.forClass(clazz)));
    }

    @Override
    public <T> Optional<IRowMapper<T>> find(Class<T> clazz, ResultSetMetaData meta, MapperConfig config) {
        return wildcard(clazz);
    }

    @Override
    public <T, V extends IRowMapper<T>> V findOrWildcard(Class<T> clazz, ResultSetMetaData meta, MapperConfig config) throws MappingException, SQLException {
        return (V) wildcard(clazz).get();
    }

    @Override
    public Optional<IRowMapper<?>> findForType(String name) {
        return Optional.empty();
    }
}
