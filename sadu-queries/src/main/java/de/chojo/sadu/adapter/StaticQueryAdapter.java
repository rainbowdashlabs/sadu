/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.adapter;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.stage.QueryStage;
import org.slf4j.Logger;

import javax.sql.DataSource;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * The StaticQueryAdapter class provides a static interface for executing queries using a query builder.
 * It allows for initializing the adapter with a data source and configuration, and provides methods for
 * building query stages.
 */

public class StaticQueryAdapter {
    private static final Logger log = getLogger(StaticQueryAdapter.class);
    private static QueryFactory factory = null;

    private StaticQueryAdapter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Creates a new query builder instance without specifying the model class.
     * This version of the builder is typically used for non-model specific queries.
     *
     * @return a query builder instance for non-model specific queries
     * @throws NotInitializedException if the adapter has not been initialized
     */
    public static QueryStage<Void> builder() {
        assertInit();
        return factory.builder();
    }

    /**
     * Creates a new query builder instance.
     *
     * @param clazz the class of the model for which the queries will be built
     * @param <T> the type of the model
     * @return a query builder instance for the specified model class
     * @throws NotInitializedException if the adapter has not been initialized
     */
    public static <T> QueryStage<T> builder(Class<T> clazz) {
        assertInit();
        return factory.builder(clazz);
    }

    /**
     * Initializes the static SADU query adapter.
     *
     * @param dataSource the data source used for executing queries
     * @param config the configuration for the query builder
     * @throws AlreadyInitializedException if the adapter has already been initialized
     */
    public static void init(DataSource dataSource, QueryBuilderConfig config) {
        if (factory != null) throw new AlreadyInitializedException();
        factory = new QueryFactory(dataSource, config);
        log.info("Static SADU query adapter started");
    }


    /**
     * Initializes the static SADU query adapter.
     *
     * @param dataSource the data source used for executing queries
     * @throws AlreadyInitializedException if the adapter has already been initialized
     */
    public static void init(DataSource dataSource) {
        if (factory != null) throw new AlreadyInitializedException();
        factory = new QueryFactory(dataSource);
        log.info("Static SADU query adapter started");
    }

    private static void assertInit() {
        if (factory == null) throw new NotInitializedException();
    }

    private static class NotInitializedException extends RuntimeException {

    }

    private static class AlreadyInitializedException extends RuntimeException {

    }
}
