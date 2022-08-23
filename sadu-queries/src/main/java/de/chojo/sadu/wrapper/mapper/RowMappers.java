/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.mapper.exceptions.MappingAlreadyRegisteredException;
import de.chojo.sadu.wrapper.mapper.exceptions.MappingException;
import de.chojo.sadu.wrapper.mapper.rowmapper.RowMapper;

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

/**
 * Class to register {@link RowMapper} to map rows to objects.
 */
public class RowMappers {
    private final Map<Class<?>, List<RowMapper<?>>> mapper = new HashMap<>();

    /**
     * Registers a new mapper for a class.
     * <p>
     * A class can only have one mapper per column combination.
     *
     * @param rowMapper the mapper to register
     * @throws MappingAlreadyRegisteredException when a mapping with the same column name exists already
     */
    public RowMappers register(RowMapper<?> rowMapper) {
        var rowMappers = mapper.computeIfAbsent(rowMapper.clazz(), key -> new ArrayList<>());
        for (var mapper : rowMappers) {
            if (mapper.columns().containsAll(rowMapper.columns())
                && mapper.columns().size() == rowMapper.columns().size()) {
                throw new MappingAlreadyRegisteredException("A mapping with pattern " + String.join(",", rowMapper.columns() + " is already registered."));
            }
        }

        rowMappers.add(rowMapper);
        return this;
    }

    /**
     * Registers new mapper.
     * <p>
     * A class can only have one mapper per column combination.
     *
     * @param rowMapper one or more mapper to register
     * @throws MappingAlreadyRegisteredException when a mapping with the same column name exists already
     */
    public RowMappers register(RowMapper<?>... rowMapper) {
        for (var mapper : rowMapper) {
            register(mapper);
        }
        return this;
    }

    private List<RowMapper<?>> mapper(Class<?> clazz) {
        return mapper.getOrDefault(clazz, Collections.emptyList());
    }

    /**
     * Gets the wild card mapper if registered.
     * @param clazz class to get the mapper
     * @return mapper if exists
     * @param <T> type of mapper
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<RowMapper<T>> wildcard(Class<T> clazz) {
        return mapper(clazz).stream()
                            .filter(RowMapper::isWildcard)
                            .findAny()
                            .map(rowMapper -> (RowMapper<T>) rowMapper);
    }

    /**
     * Searches for a matching mapper for this result set.
     * <p>
     * A mapper is matching when all columns the mapper needs are present in the result set.
     * <p>
     * The mapper with the most matching columns will be returned.
     *
     * @param clazz clazz to find a mapper for
     * @param set  result set to find a matching mapper
     * @param <T>   return type of mapper
     * @return mapper when found
     */
    public <T> Optional<RowMapper<T>> find(Class<T> clazz, ResultSet set, MapperConfig config) throws SQLException {
        return find(clazz, set.getMetaData(), config);
    }

    /**
     * Searches for a matching mapper for this result set.
     * <p>
     * A mapper is matching when all columns the mapper needs are present in the result set.
     * <p>
     * The mapper with the most matching columns will be returned.
     *
     * @param clazz clazz to find a mapper for
     * @param meta  result set meta to find a matching mapper
     * @param <T>   return type of mapper
     * @return mapper when found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<RowMapper<T>> find(Class<T> clazz, ResultSetMetaData meta, MapperConfig config) {
        return mapper(clazz)
                .stream()
                .filter(mapper -> !mapper.isWildcard())
                .map(mapper -> Map.entry(mapper, mapper.applicable(meta, config.isStrict())))
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(rowMapper -> (RowMapper<T>) rowMapper);
    }

    /**
     * Searches for a matching mapper for this result set.
     * <p>
     * A mapper is matching when all columns the mapper needs are present in the result set.
     * <p>
     * The mapper with the most matching columns will be returned.
     *
     * @param <T>    return type of mapper
     * @param clazz  clazz to find a mapper for
     * @param set    result set to find a matching mapper
     * @param config mapper config
     * @return mapper when found
     * @throws MappingException when no mapper was found for this class and no wildcard mapper is registered.
     */
    public <T> RowMapper<T> findOrWildcard(Class<T> clazz, ResultSet set, MapperConfig config) throws MappingException, SQLException {
        return findOrWildcard(clazz, set.getMetaData(), config);
    }

    /**
     * Finds a mapper for a class for the matching column names.
     * If no mapper was found a possible wildcard mapper will be returned.
     *
     * @param clazz clazz
     * @param meta  meta
     * @param <T>   type of row mapper
     * @return row mapper when found
     * @throws MappingException when no mapper was found for this class and no wildcard mapper is registered.
     * @throws SQLException     if a database access error occurs
     */
    public <T> RowMapper<T> findOrWildcard(Class<T> clazz, ResultSetMetaData meta, MapperConfig config) throws MappingException, SQLException {
        Optional<? extends RowMapper<T>> mapper = find(clazz, meta, config)
                .or(() -> wildcard(clazz));
        if (mapper.isPresent()) {
            return mapper.get();
        }
        throw MappingException.create(meta);
    }
}
