/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.exceptions.ExceptionTransformer;
import de.chojo.sadu.mapper.RowMapperRegistry;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Consumer;

public class QueryConfigurationBuilder {
    private @Nullable DataSource dataSource;
    private boolean atomic = true;
    private boolean throwExceptions;
    @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
    private Consumer<SQLException> exceptionHandler = throwable -> {
        System.err.println(ExceptionTransformer.prettyException(throwable));
        throwable.printStackTrace();
    };
    private RowMapperRegistry rowMapperRegistry = new RowMapperRegistry();

    public QueryConfigurationBuilder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public QueryConfigurationBuilder setDataSource(@Nullable DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public QueryConfigurationBuilder setAtomic(boolean atomic) {
        this.atomic = atomic;
        return this;
    }

    public QueryConfigurationBuilder setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
        return this;
    }

    public QueryConfigurationBuilder setExceptionHandler(Consumer<SQLException> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public QueryConfigurationBuilder setRowMapperRegistry(RowMapperRegistry rowMapperRegistry) {
        this.rowMapperRegistry = rowMapperRegistry;
        return this;
    }

    public QueryConfiguration build() {
        return new QueryConfiguration(dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }
}
