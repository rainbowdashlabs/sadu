/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.mapper.exceptions.MappingException;
import de.chojo.sadu.mapper.rowmapper.IRowMapper;
import de.chojo.sadu.mapper.rowmapper.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRowMapperRegistry {
    List<? extends RowMapper<?>> mapper(Class<?> clazz);

    @SuppressWarnings("unchecked")
    <T> Optional<? extends IRowMapper<T>> wildcard(Class<T> clazz);

    default <T> Optional<? extends IRowMapper<T>> find(Class<T> clazz, ResultSet set, MapperConfig config) throws SQLException {
        return find(clazz, set.getMetaData(), config);
    }

    @SuppressWarnings("unchecked")
    <T> Optional<? extends IRowMapper<T>> find(Class<T> clazz, ResultSetMetaData meta, MapperConfig config);

    default <T, V extends IRowMapper<T>> V findOrWildcard(Class<T> clazz, ResultSet set, MapperConfig config) throws MappingException, SQLException {
        return findOrWildcard(clazz, set.getMetaData(), config);
    }

    <T, V extends IRowMapper<T>> V findOrWildcard(Class<T> clazz, ResultSetMetaData meta, MapperConfig config) throws MappingException, SQLException;

    Optional<? extends IRowMapper<?>> findForType(String name);
}
