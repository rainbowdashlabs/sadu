/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.core.exceptions.ExceptionTransformer;
import de.chojo.sadu.mapper.RowMapperRegistry;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Consumer;

public class QueryConfigurationBuilder {
    private final DataSource dataSource;
    private boolean atomic = true;
    private boolean throwExceptions;
    @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
    private Consumer<SQLException> exceptionHandler = throwable -> {
        System.err.println(ExceptionTransformer.prettyException(throwable));
        throwable.printStackTrace();
    };
    private RowMapperRegistry rowMapperRegistry = new RowMapperRegistry();

    /**
     * QueryConfigurationBuilder is a builder class used to create an instance of QueryConfiguration.
     * It provides methods to configure various properties of the QueryConfiguration.
     */
    public QueryConfigurationBuilder(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the atomic flag for the QueryConfigurationBuilder.
     * <p>
     * Default: true
     *
     * @param atomic the value to set the atomic flag to
     * @return the QueryConfigurationBuilder instance
     */
    public QueryConfigurationBuilder setAtomic(boolean atomic) {
        this.atomic = atomic;
        return this;
    }

    /**
     * Sets the flag indicating whether exceptions thrown during database queries should be thrown or logged.
     *
     * @param throwExceptions flag indicating whether exceptions should be thrown (true) or logged (false)
     * @return the QueryConfigurationBuilder instance
     */
    public QueryConfigurationBuilder setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
        return this;
    }

    /**
     * Sets the exception handler for handling SQLExceptions that may occur during database operations.
     * <p>
     * Default: stdout
     *
     * @param exceptionHandler the consumer function that handles SQLExceptions
     * @return the QueryConfigurationBuilder instance
     */
    public QueryConfigurationBuilder setExceptionHandler(Consumer<SQLException> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Sets the {@link RowMapperRegistry} for the {@link QueryConfiguration}.
     *
     * @param rowMapperRegistry the RowMapperRegistry to set
     * @return the QueryConfigurationBuilder instance
     */
    public QueryConfigurationBuilder setRowMapperRegistry(RowMapperRegistry rowMapperRegistry) {
        this.rowMapperRegistry = rowMapperRegistry;
        return this;
    }

    /**
     * Returns a new {@link QueryConfiguration} object based on the current configuration settings.
     *
     * @return a new {@link QueryConfiguration} object
     */
    public QueryConfiguration build() {
        return new QueryConfiguration(dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }
}
