/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.base;

import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.stage.QueryStage;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class which provides a factory for easy usage.
 * <p>
 * Can be used instead of a {@link DataHolder}
 */
public class QueryFactory extends DataHolder {
    private final AtomicReference<QueryBuilderConfig> config;

    /**
     * Create a new QueryFactory
     *
     * @param dataSource datasource
     * @param config     factory config
     */
    public QueryFactory(DataSource dataSource, QueryBuilderConfig config) {
        this(dataSource, new AtomicReference<>(config));
    }

    private QueryFactory(DataSource dataSource, AtomicReference<QueryBuilderConfig> config) {
        super(dataSource);
        this.config = config;
    }

    /**
     * Create a new QueryFactory
     *
     * @param dataSource datasource
     */
    public QueryFactory(DataSource dataSource) {
        this(dataSource, QueryBuilderConfig.defaultConfig());
    }

    /**
     * Creates a QueryFactory and uses the {@link DataSource} contained in the {@link DataSourceProvider}.
     *
     * @param provider provider
     * @param config   factory config
     */
    public QueryFactory(DataSourceProvider provider, QueryBuilderConfig config) {
        this(provider.source(), config);
    }

    /**
     * Creates a QueryFactory and uses the {@link DataSource} contained in the {@link DataSourceProvider}.
     *
     * @param provider provider
     */
    public QueryFactory(DataSourceProvider provider) {
        this(provider.source());
    }

    /**
     * Creates a QueryFactory based on the passed QueryFactory.
     * <p>
     * Configuration will be copied.
     *
     * @param factoryHolder parent factory holder
     */
    public QueryFactory(QueryFactory factoryHolder) {
        this(factoryHolder.source(), factoryHolder.config());
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param clazz class of required return type. Doesn't matter if you want a list or single result.
     * @param <T>   type if result as class
     * @return a new query builder in a {@link QueryStage}
     */
    public <T> QueryStage<T> builder(Class<T> clazz) {
        return QueryBuilder.builder(source(), clazz).configure(config);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @return a new query builder in a {@link QueryStage}
     */
    public QueryStage<Void> builder() {
        return QueryBuilder.builder(source(), Void.class).configure(config);
    }

    /**
     * Gets the underlying config.
     *
     * @return config reference
     */
    public AtomicReference<QueryBuilderConfig> config() {
        return config;
    }

}
