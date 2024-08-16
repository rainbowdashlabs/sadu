/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.ActiveQueryConfiguration;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.api.configuration.context.QueryContext;
import de.chojo.sadu.queries.configuration.context.SimpleQueryContext;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Consumer;

public class QueryConfigurationImpl implements QueryConfiguration {
    protected final @NotNull DataSource dataSource;
    protected final boolean atomic;
    protected final boolean throwExceptions;
    protected final Consumer<SQLException> exceptionHandler;
    protected final RowMapperRegistry rowMapperRegistry;

    QueryConfigurationImpl(@NotNull DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    @Override
    public ActiveQueryConfiguration forQuery(QueryContext context) {
        return new ActiveQueryConfigurationImpl(dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry, context);
    }

    @Override
    public boolean atomic() {
        return atomic;
    }

    @Override
    public boolean throwExceptions() {
        return throwExceptions;
    }

    @Override
    public RowMapperRegistry rowMapperRegistry() {
        return rowMapperRegistry;
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public Consumer<SQLException> exceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        return Query.query(this, sql, format);
    }

    @Override
    public ConnectedQueryConfigurationImpl withSingleTransaction() {
        return new ConnectedQueryConfigurationImpl(new SimpleQueryContext(null), dataSource, null, true, throwExceptions, exceptionHandler, rowMapperRegistry);
    }
}
