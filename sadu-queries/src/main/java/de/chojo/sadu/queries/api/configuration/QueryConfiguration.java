/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.context.QueryContext;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.configuration.ConnectedQueryConfigurationImpl;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.intellij.lang.annotations.Language;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("removal")
public interface QueryConfiguration extends de.chojo.sadu.queries.configuration.QueryConfiguration {
    AtomicReference<QueryConfiguration> DEFAULT = new AtomicReference<>(null);

    /**
     * Retrieves the default QueryConfiguration.
     *
     * @return the default QueryConfiguration
     */
    static QueryConfiguration getDefault() {
        return Objects.requireNonNull(DEFAULT.get(), "You need to configure the configuration first by calling QueryConfiguration.setDefault()");
    }

    /**
     * Sets the default query configuration.
     *
     * @param configuration the query configuration to set as default
     */
    static void setDefault(QueryConfiguration configuration) {
        DEFAULT.set(configuration);
    }

    default QueryConfigurationBuilder edit(DataSource source) {
        return builder(source)
                .setAtomic(atomic())
                .setExceptionHandler(exceptionHandler())
                .setRowMapperRegistry(rowMapperRegistry())
                .setThrowExceptions(throwExceptions());
    }

    default QueryConfigurationBuilder edit() {
        return edit(dataSource());
    }

    /**
     * Creates a new QueryConfigurationBuilder instance with the given DataSource.
     *
     * @param source the DataSource to use for QueryConfiguration
     * @return a QueryConfigurationBuilder instance
     */
    static QueryConfigurationBuilder builder(DataSource source) {
        return new QueryConfigurationBuilder(source);
    }

    /**
     * Returns a new QueryConfiguration object with the provided query and other configuration settings.
     *
     * @param context the query to be associated with the configuration
     * @return the new QueryConfiguration object
     */
    ActiveQueryConfiguration forQuery(QueryContext context);

    /**
     * Retrieves the value of the atomic flag.
     *
     * @return {@code true} if atomic flag is set; {@code false} otherwise.
     */
    boolean atomic();

    /**
     * Retrieves the value of the throwExceptions field.
     *
     * @return the value of the throwExceptions field
     */
    boolean throwExceptions();

    /**
     * Retrieves the {@link RowMapperRegistry} object from the QueryConfiguration.
     *
     * @return The {@link RowMapperRegistry} object from the QueryConfiguration.
     */
    RowMapperRegistry rowMapperRegistry();

    /**
     * Returns the DataSource object associated with this QueryConfiguration.
     *
     * @return the DataSource object
     */
    DataSource dataSource();

    /**
     * Retrieves the exception handler
     *
     * @return exception handler
     */
    Consumer<SQLException> exceptionHandler();

    /**
     * Executes a SQL query with the given SQL statement and format arguments.
     *
     * @param sql    the SQL statement to be executed
     * @param format the format arguments to be applied to the SQL statement
     * @return a parsed query ready for execution
     */
    ParsedQuery query(@Language("sql") String sql, Object... format);

    /**
     * Returns a new instance of the ConnectedQueryConfiguration class with the "single transaction" configuration applied.
     * This means that a single transaction will be used for the queries executed using this configuration.
     *
     * @return A new instance of the ConnectedQueryConfiguration class with the "single transaction" configuration applied.
     */
    ConnectedQueryConfigurationImpl withSingleTransaction();
}
