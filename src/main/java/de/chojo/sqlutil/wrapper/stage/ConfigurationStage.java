/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.wrapper.stage;

import de.chojo.sqlutil.wrapper.QueryBuilder;
import de.chojo.sqlutil.wrapper.QueryBuilderConfig;
import de.chojo.sqlutil.wrapper.QueryBuilderFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Configuration stage of a {@link QueryBuilder}
 *
 * @param <T> type
 */
public interface ConfigurationStage<T> {
    /**
     * Configure the query builder.
     * <p>
     * A configured {@link QueryBuilderFactory} can be used to skip this step.
     *
     * @param config The config of the {@link QueryBuilder}
     * @return The {@link QueryBuilder} in {@link QueryStage} with the config set.
     */
    default QueryStage<T> configure(QueryBuilderConfig config) {
        return configure(new AtomicReference<>(config));
    }

    /**
     * Configure the query builder.
     * <p>
     * A configured {@link QueryBuilderFactory} can be used to skip this step.
     *
     * @param config The config of the {@link QueryBuilder}
     * @return The {@link QueryBuilder} in {@link QueryStage} with the config set.
     */
    QueryStage<T> configure(AtomicReference<QueryBuilderConfig> config);

    /**
     * Set the settings to default values.
     * <p>
     * Default will be atomic transactions and not throwing any exception.
     * <p>
     * A configured {@link QueryBuilderFactory} can be used to skip this step.
     *
     * @return The {@link QueryBuilder} in
     */
    QueryStage<T> defaultConfig();


}
