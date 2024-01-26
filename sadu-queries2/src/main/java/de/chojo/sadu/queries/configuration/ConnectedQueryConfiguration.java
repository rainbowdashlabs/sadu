/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import de.chojo.sadu.queries.stages.QueryImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class ConnectedQueryConfiguration extends QueryConfiguration {
    private Connection connection = null;

    ConnectedQueryConfiguration(QueryImpl query, DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        super(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    public ConnectedQueryConfiguration forQuery(QueryImpl query) {
        return new ConnectedQueryConfiguration(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                if (query.exceptions().isEmpty()) {
                    if (atomic()) {
                        // TODO commit if atomic
                        connection.commit();
                    }
                }
                connection.close();
            }
        } catch (SQLException e) {
            handleException(e);
        }
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

    @Override
    public Connection connection() {
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(!atomic);
            } catch (SQLException e) {
                handleException(e);
            }
        }
        return connection;
    }

    @Override
    public boolean hasConnection() {
        return true;
    }
}
