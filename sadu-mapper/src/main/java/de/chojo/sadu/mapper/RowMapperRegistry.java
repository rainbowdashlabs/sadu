/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.mapper.annotation.MappingProvider;
import de.chojo.sadu.mapper.exceptions.InvalidMappingException;
import de.chojo.sadu.mapper.exceptions.MappingAlreadyRegisteredException;
import de.chojo.sadu.mapper.exceptions.MappingException;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.mapper.wrapper.Row;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class to register {@link RowMapper} to map rows to objects.
 */
public class RowMapperRegistry {
    private final Map<Class<?>, List<RowMapper<?>>> mapper = new HashMap<>();
    private static final Logger log = getLogger(RowMapperRegistry.class);

    public RowMapperRegistry() {
    }

    /**
     * Registers a new mapper for a class.
     * <p>
     * A class can only have one mapper per column combination.
     *
     * @param rowMapper the mapper to register
     * @throws MappingAlreadyRegisteredException when a mapping with the same column name exists already
     */
    public RowMapperRegistry register(RowMapper<?> rowMapper) {
        var rowMappers = mapper.computeIfAbsent(rowMapper.clazz(), key -> new ArrayList<>());
        for (var mapper : rowMappers) {
            if (mapper.columns().equals(rowMapper.columns())) {
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
    public RowMapperRegistry register(RowMapper<?>... rowMapper) {
        for (var mapper : rowMapper) {
            //noinspection ResultOfMethodCallIgnored
            register(mapper);
        }
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
    public RowMapperRegistry register(List<RowMapper<?>> rowMapper) {
        for (var mapper : rowMapper) {
            //noinspection ResultOfMethodCallIgnored
            register(mapper);
        }
        return this;
    }

    private List<RowMapper<?>> mapper(Class<?> clazz) {
        return mapper.getOrDefault(clazz, Collections.emptyList());
    }

    /**
     * Gets the wild card mapper if registered.
     *
     * @param clazz class to get the mapper
     * @param <T>   type of mapper
     * @return mapper if exists
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
     * @param set   result set to find a matching mapper
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
                .map(mapper -> Map.entry(mapper, mapper.applicable(meta, config)))
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

        // Autodetect mapper if available
        if (registerInternal(clazz)) {
            return findOrWildcard(clazz, meta, config);
        }

        throw MappingException.create(clazz, meta);
    }

    /**
     * Registers row mapper of a class if they contain a constructor or static method annotated with {@link MappingProvider}
     *
     * @param clazz clazz to be registered
     * @param <V>   type of class to be registered
     * @throws InvalidMappingException when no mapping was found.
     */
    public <V> void register(Class<V> clazz) {
        if (registerInternal(clazz)) return;
        throw new InvalidMappingException("No mapping was detected for class %s".formatted(clazz.getName()));
    }

    private <V> boolean registerInternal(Class<V> clazz) {
        boolean method = discoverMethodMapping(clazz);
        boolean constructor = discoverConstructorMapping(clazz);
        return method || constructor;
    }

    @SuppressWarnings("unchecked")
    private <V> boolean discoverMethodMapping(Class<V> clazz) {
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                                     .filter(method -> method.isAnnotationPresent(MappingProvider.class) && Modifier.isStatic(method.getModifiers()))
                                     .toList();

        for (Method method : methods) {
            MappingProvider provider = method.getAnnotation(MappingProvider.class);
            RowMapping<V> mapper;
            try {
                mapper = (RowMapping<V>) method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                throw new InvalidMappingException("Could not retrieve mapping. Method has to return %s<%s> and take no arguments".formatted(RowMapping.class.getName(), clazz.getName()), e);
            }
            register(RowMapper.forClass(clazz).mapper(mapper).addColumns(provider.value()).build());
            log.info("Registered method auto mapping for {} with rows {}", clazz.getName(), provider.value());

        }
        return !methods.isEmpty();
    }

    @SuppressWarnings("unchecked")
    private <V> boolean discoverConstructorMapping(Class<V> clazz) {
        List<Constructor<?>> constructors = Arrays.stream(clazz.getDeclaredConstructors())
                                                  .filter(constr -> constr.isAnnotationPresent(MappingProvider.class))
                                                  .toList();

        for (Constructor<?> constructor : constructors) {
            MappingProvider provider = constructor.getAnnotation(MappingProvider.class);
            if (constructor.getParameterCount() != 1) {
                throw new InvalidMappingException("Signature of a constructor with MappingProvider should be Constructor(Row)");
            }
            if (constructor.getParameterTypes()[0] != Row.class) {
                throw new InvalidMappingException("Signature of a constructor with MappingProvider should be Constructor(Row), but was Constructor(%s)".formatted(constructor.getParameterTypes()[0].getName()));
            }

            RowMapper<V> mapper = RowMapper.forClass(clazz).mapper(row -> {
                try {
                    return (V) constructor.newInstance(row);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new InvalidMappingException("Failed to create instance from RowMapping via constructor");
                }
            }).addColumns(provider.value()).build();

            try {
                register(mapper);
                log.info("Registered constructor auto mapping for {} with rows {}", clazz.getName(), provider.value());
            } catch (MappingAlreadyRegisteredException e) {
                // This case mostly happens if multiple threads are accessing the database at the same time.
                // We do not consider this an error, since the mapping was still properly registered on another thread.
                log.debug("Mapping already registered for {} with rows {}. Using already registered mapping", clazz.getName(), provider.value());
            }
        }
        return !constructors.isEmpty();
    }
}
