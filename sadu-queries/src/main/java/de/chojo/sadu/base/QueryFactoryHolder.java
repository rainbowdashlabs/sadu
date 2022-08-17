/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.base;

import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.QueryBuilderFactory;
import de.chojo.sadu.wrapper.stage.QueryStage;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class which provides a factory for easy usage.
 * <p>
 * Can be used instead of a {@link DataHolder}
 */
public abstract class QueryFactoryHolder extends DataHolder {
    private final QueryBuilderFactory factory;

    /**
     * Create a new QueryFactoryholder
     *
     * @param dataSource datasource
     * @param config     factory config
     */
    public QueryFactoryHolder(DataSource dataSource, QueryBuilderConfig config) {
        this(dataSource, new AtomicReference<>(config));
    }

    private QueryFactoryHolder(DataSource dataSource, AtomicReference<QueryBuilderConfig> config) {
        super(dataSource);
        factory = new QueryBuilderFactory(config, dataSource);
    }

    /**
     * Create a new QueryFactoryholder
     *
     * @param dataSource datasource
     */
    public QueryFactoryHolder(DataSource dataSource) {
        this(dataSource, QueryBuilderConfig.defaultConfig());
    }

    /**
     * Creates a {@link QueryFactoryHolder} and uses the {@link DataSource} contained in the {@link DataSourceProvider}.
     *
     * @param provider provider
     * @param config   factory config
     */
    public QueryFactoryHolder(DataSourceProvider provider, QueryBuilderConfig config) {
        this(provider.source(), config);
    }

    /**
     * Creates a {@link QueryFactoryHolder} and uses the {@link DataSource} contained in the {@link DataSourceProvider}.
     *
     * @param provider provider
     */
    public QueryFactoryHolder(DataSourceProvider provider) {
        this(provider.source());
    }

    /**
     * Creates a {@link QueryFactoryHolder} based on the passed {@link QueryFactoryHolder}.
     * <p>
     * Configuration will be copied.
     *
     * @param factoryHolder parent factory holder
     */
    public QueryFactoryHolder(QueryFactoryHolder factoryHolder) {
        this(factoryHolder.source(), factoryHolder.factory().config());
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param clazz class of required return type. Doesn't matter if you want a list or single result.
     * @param <T>   type if result as class
     * @return a new query builder in a {@link QueryStage}
     */
    public <T> QueryStage<T> builder(Class<T> clazz) {
        return factory.builder(clazz);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @return a new query builder in a {@link QueryStage}
     */
    public QueryStage<Void> builder() {
        return factory.builder();
    }

    /**
     * Get the underlying factory
     *
     * @return query factory
     */
    public QueryBuilderFactory factory() {
        return factory;
    }

    /**
     * Get the underlying data source
     *
     * @return datasource
     */
    public DataSource source() {
        return factory.source();
    }
}
