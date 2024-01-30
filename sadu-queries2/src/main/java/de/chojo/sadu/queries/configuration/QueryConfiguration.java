/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import de.chojo.sadu.queries.stages.QueryImpl;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class QueryConfiguration {
    static final AtomicReference<QueryConfiguration> DEFAULT = new AtomicReference<>(null);
    protected final QueryImpl query;
    protected final @NotNull DataSource dataSource;
    protected final boolean atomic;
    protected final boolean throwExceptions;
    protected final Consumer<SQLException> exceptionHandler;
    protected final RowMapperRegistry rowMapperRegistry;

    QueryConfiguration(@NotNull DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        query = null;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    QueryConfiguration(QueryImpl query, @NotNull DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        this.query = query;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    /**
     * Retrieves the default QueryConfiguration.
     *
     * @return the default QueryConfiguration
     */
    public static QueryConfiguration getDefault() {
        return Objects.requireNonNull(DEFAULT.get(), "You need to configure the configuration first by calling QueryConfiguration.setDefault()");
    }

    /**
     * Sets the default query configuration.
     *
     * @param configuration the query configuration to set as default
     */
    public static void setDefault(QueryConfiguration configuration) {
        DEFAULT.set(configuration);
    }

    /**
     * Creates a new QueryConfigurationBuilder instance with the given DataSource.
     *
     * @param source the DataSource to use for QueryConfiguration
     * @return a QueryConfigurationBuilder instance
     */
    public static QueryConfigurationBuilder builder(DataSource source) {
        return new QueryConfigurationBuilder(source);
    }

    /**
     * Returns a new QueryConfiguration object with the provided query and other configuration settings.
     *
     * @param query the query to be associated with the configuration
     * @return the new QueryConfiguration object
     */
    public QueryConfiguration forQuery(QueryImpl query) {
        return new QueryConfiguration(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    /**
     * Handles a SQLException by invoking the exceptionHandler consumer, logging the exception,
     * and potentially throwing a WrappedQueryExecutionException.
     *
     * @param e the SQLException to handle
     */
    public void handleException(SQLException e) {
        exceptionHandler.accept(e);
        query.logException(e);
        if (throwExceptions) {
            throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
        }
    }

    /**
     * Retrieves the value of the atomic flag.
     *
     * @return {@code true} if atomic flag is set; {@code false} otherwise.
     */
    public boolean atomic() {
        return atomic;
    }

    /**
     * Retrieves the value of the throwExceptions field.
     *
     * @return the value of the throwExceptions field
     */
    public boolean throwExceptions() {
        return throwExceptions;
    }

    /**
     * Retrieves the {@link RowMapperRegistry} object from the {@link QueryConfiguration}.
     *
     * @return The {@link RowMapperRegistry} object from the {@link QueryConfiguration}.
     */
    public RowMapperRegistry rowMapperRegistry() {
        return rowMapperRegistry;
    }

    /**
     * Returns the DataSource object associated with this QueryConfiguration.
     *
     * @return the DataSource object
     */
    public DataSource dataSource() {
        return dataSource;
    }

    /**
     * Executes a SQL query with the given SQL statement and format arguments.
     *
     * @param sql    the SQL statement to be executed
     * @param format the format arguments to be applied to the SQL statement
     * @return a parsed query ready for execution
     */
    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        return Query.query(this, sql, format);
    }

    /**
     * Returns a new instance of the ConnectedQueryConfiguration class with the "single transaction" configuration applied.
     * This means that a single transaction will be used for the queries executed using this configuration.
     *
     * @return A new instance of the ConnectedQueryConfiguration class with the "single transaction" configuration applied.
     */
    public ConnectedQueryConfiguration withSingleTransaction() {
        return new ConnectedQueryConfiguration(query, dataSource, true, throwExceptions, exceptionHandler, rowMapperRegistry);
    }
}
