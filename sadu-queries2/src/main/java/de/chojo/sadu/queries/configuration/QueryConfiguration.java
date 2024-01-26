/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.ParsedQuery;
import de.chojo.sadu.queries.api.Query;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import de.chojo.sadu.queries.stages.QueryImpl;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class QueryConfiguration {
    static final AtomicReference<QueryConfiguration> DEFAULT = new AtomicReference<>(null);
    protected final QueryImpl query;
    protected final DataSource dataSource;
    protected final boolean atomic;
    protected final boolean throwExceptions;
    protected final Consumer<SQLException> exceptionHandler;
    protected final RowMapperRegistry rowMapperRegistry;

    QueryConfiguration(@Nullable DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        query = null;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    QueryConfiguration(QueryImpl query, DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        this.query = query;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    public static QueryConfiguration getDefault() {
        return DEFAULT.get();
    }

    public static void setDefault(QueryConfiguration configuration) {
        DEFAULT.set(configuration);
    }

    public QueryConfiguration forQuery(QueryImpl query) {
        return new QueryConfiguration(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    public void handleException(SQLException e) {
        exceptionHandler.accept(e);
        query.logException(e);
        if (throwExceptions) {
            throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
        }
    }

    public boolean atomic() {
        return atomic;
    }

    public boolean throwExceptions() {
        return throwExceptions;
    }

    public RowMapperRegistry rowMapperRegistry() {
        return rowMapperRegistry;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public Connection connection() {
        return null;
    }

    public boolean hasConnection() {
        return false;
    }

    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        return Query.query(this, sql, format);
    }

    public QueryConfiguration withSingleTransaction() {
        return new ConnectedQueryConfiguration(query, dataSource, true, throwExceptions, exceptionHandler, rowMapperRegistry);
    }
}
