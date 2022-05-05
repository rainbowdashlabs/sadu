/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.wrapper;

import de.chojo.sqlutil.wrapper.stage.ConfigurationStage;
import de.chojo.sqlutil.wrapper.stage.QueryStage;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class provides simple methods to create preconfigured {@link QueryBuilder}.
 * <p>
 * The factory will always initialize the {@link QueryBuilder} with the plugin, datasource and configuration provided on creation.
 * <p>
 * This results in a QueryBuilder in the {@link QueryStage} and skips the {@link ConfigurationStage}.
 */
public class QueryBuilderFactory {
    private final AtomicReference<QueryBuilderConfig> config;
    private final DataSource dataSource;

    /**
     * Crea a new QueryBuilderFactory
     *
     * @param config     configuration
     * @param dataSource data source
     */
    public QueryBuilderFactory(AtomicReference<QueryBuilderConfig> config, DataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
    }

    public QueryBuilderFactory(DataSource dataSource) {
        this(QueryBuilderConfig.defaultConfig(), dataSource);
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param clazz class of required return type. Doesnt matter if you want a list or single result.
     * @param <T>   type of return type
     * @return a new query builder in a {@link QueryStage}
     */
    public <T> QueryStage<T> builder(Class<T> clazz) {
        return QueryBuilder.builder(dataSource, clazz).configure(config);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @return a new query builder in a {@link QueryStage}
     */
    public QueryStage<Void> builder() {
        return QueryBuilder.builder(dataSource, Void.class).configure(config);
    }

    public DataSource source() {
        return dataSource;
    }
}
