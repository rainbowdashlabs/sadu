/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.wrapper;

import de.chojo.sqlutil.exceptions.ExceptionTransformer;
import de.chojo.sqlutil.wrapper.exception.QueryExecutionException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Configuration for a {@link QueryBuilder}
 */
public class QueryBuilderConfig {
    /**
     * Contains the default configuration.
     */
    private static final AtomicReference<QueryBuilderConfig> DEFAULT = new AtomicReference<>(builder().build());

    private final boolean throwing;
    private final boolean atomic;
    private final Consumer<SQLException> exceptionHandler;
    private final ExecutorService executor;

    private QueryBuilderConfig(boolean throwing, boolean atomic, Consumer<SQLException> exceptionHandler, ExecutorService executorService) {
        this.throwing = throwing;
        this.atomic = atomic;
        this.exceptionHandler = exceptionHandler;
        this.executor = executorService;
    }

    /**
     * Get a builder for a {@link QueryBuilderConfig}
     *
     * @return new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Check if the config is throwing
     *
     * @return true if throwing
     */
    public boolean isThrowing() {
        return throwing;
    }

    /**
     * Checks if atomic transactions should be used.
     *
     * @return true if atomic
     */
    public boolean isAtomic() {
        return atomic;
    }

    public Optional<Consumer<SQLException>> exceptionHandler() {
        return Optional.ofNullable(exceptionHandler);
    }

    public ExecutorService executor() {
        return executor;
    }

    /**
     * Builder for a {@link QueryBuilderConfig}
     */
    public static class Builder {
        private boolean throwing;
        private boolean atomic = true;
        private Consumer<SQLException> exceptionHandler = throwables -> {
            System.err.println(ExceptionTransformer.prettyException(throwables));
            throwables.printStackTrace();
        };

        private ExecutorService executorService = ForkJoinPool.commonPool();

        /**
         * Sets the query builder as throwing. This will cause any occuring exception to be wrapped into an {@link QueryExecutionException} and be thrown instead of logged.
         *
         * @return The {@link Builder} with the value set.
         */
        public Builder throwExceptions() {
            throwing = true;
            return this;
        }

        /**
         * Sets the query builder exception handler. This will only have an effect if {@link #throwExceptions()} is not called.
         *
         * @param exceptionHandler handler for exception
         * @return The {@link Builder} with the value set.
         */
        public Builder withExceptionHandler(Consumer<SQLException> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        /**
         * Disable the default logger.
         *
         * @return builder instance
         */
        public Builder disableDefaultLogger() {
            this.exceptionHandler = null;
            return this;
        }

        /**
         * Set that the queries are not executed atomic.
         * <p>
         * When the queries are atomic they will be executed in one transaction. This will cause that no data will be changed if any query fails to execute.
         * <p>
         * On default queries will be also executed atomic. This method just exists for convenience. No queries will be executed after one query fails in any way.
         * <p>
         *
         * @return The {@link Builder} in with the atomic value set.
         */
        public Builder notAtomic() {
            atomic = false;
            return this;
        }

        /**
         * Sets the exector service used for the completable futures.
         *
         * @param executorService executor service
         * @return The {@link Builder} in with the executor set.
         */
        public Builder withExecutor(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        /**
         * Retrieve a new {@link QueryBuilderConfig} instance.
         *
         * @return config with defined values
         */
        public QueryBuilderConfig build() {
            return new QueryBuilderConfig(throwing, atomic, exceptionHandler, executorService);
        }
    }

    public static void setDefault(QueryBuilderConfig config) {
        DEFAULT.set(config);
    }

    public static AtomicReference<QueryBuilderConfig> defaultConfig() {
        return DEFAULT;
    }
}
