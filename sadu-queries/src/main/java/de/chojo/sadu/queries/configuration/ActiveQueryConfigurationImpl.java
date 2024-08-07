/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.ActiveQueryConfiguration;
import de.chojo.sadu.queries.api.configuration.context.QueryContext;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Consumer;

public class ActiveQueryConfigurationImpl extends QueryConfigurationImpl implements ActiveQueryConfiguration {
    protected final @NotNull QueryContext context;

    public ActiveQueryConfigurationImpl(@NotNull DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry, @NotNull QueryContext context) {
        super(dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
        this.context = context;
    }

    @Override
    public void handleException(SQLException e) {
        exceptionHandler.accept(e);
        context.logException(e);
        if (throwExceptions) {
            throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
        }
    }

    public @NotNull QueryContext context() {
        return context;
    }
}
