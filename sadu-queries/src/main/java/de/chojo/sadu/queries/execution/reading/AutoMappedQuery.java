/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.execution.reading;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.execution.writing.CalledSingletonQueryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that represents a query that will be mapped via a {@link RowMapperRegistry}
 *
 * @param <V> Type of result object
 */
public class AutoMappedQuery<V> extends ReaderImpl<V> {
    private final Class<V> clazz;
    private final MapperConfig config;

    public AutoMappedQuery(CalledSingletonQueryImpl query, Class<V> clazz, MapperConfig config) {
        super(query);
        this.clazz = clazz;
        this.config = config;
    }

    @Override
    protected RowMapping<V> mapper(ResultSet set) throws SQLException {
        return query().configuration().rowMapperRegistry().findOrWildcard(clazz, set, config);
    }

    @Override
    protected MapperConfig mapperConfig() {
        return config;
    }
}
