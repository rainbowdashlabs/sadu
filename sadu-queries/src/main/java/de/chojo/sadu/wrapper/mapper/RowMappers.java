/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.mapper.exceptions.MappingException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class RowMappers {
    private static final Map<Class<?>, List<RowMapper<?>>> MAPPER = new HashMap<>();

    private RowMappers() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static void register(RowMapper<?> rowMapper) {
        MAPPER.computeIfAbsent(rowMapper.clazz(), key -> new ArrayList<>()).add(rowMapper);
    }

    private static List<RowMapper<?>> mapper(Class<?> clazz) {
        return MAPPER.getOrDefault(clazz, Collections.emptyList());
    }

    public static <T> Optional<RowMapper<T>> wildcard(Class<T> clazz) {
        return mapper(clazz).stream()
                            .filter(RowMapper::isWildcard)
                            .findAny()
                            .map(rowMapper -> (RowMapper<T>) rowMapper);
    }

    public static <T> Optional<RowMapper<T>> find(Class<T> clazz, ResultSet set) throws SQLException {
        return find(clazz, set.getMetaData());
    }

    public static <T> Optional<RowMapper<T>> find(Class<T> clazz, ResultSetMetaData meta) {
        return mapper(clazz)
                .stream()
                .filter(mapper -> !mapper.isWildcard())
                .map(mapper -> Map.entry(mapper, mapper.applicable(meta)))
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(rowMapper -> (RowMapper<T>) rowMapper);
    }

    public static <T> RowMapper<T> findOrWildcard(Class<T> clazz, ResultSet set) throws MappingException, SQLException {
        return findOrWildcard(clazz, set.getMetaData());
    }

    public static <T> RowMapper<T> findOrWildcard(Class<T> clazz, ResultSetMetaData meta) throws MappingException, SQLException {
        Optional<? extends RowMapper<T>> mapper = find(clazz, meta)
                .or(() -> wildcard(clazz));
        if(mapper.isPresent()){
            return mapper.get();
        }
        throw new MappingException(meta);
    }
}
