/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import de.chojo.sadu.queries.query.QueryImpl;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class ConnectedQueryConfigurationImpl extends QueryConfigurationImpl implements ConnectedQueryConfiguration {
    private Connection connection;

    ConnectedQueryConfigurationImpl(QueryImpl query, DataSource dataSource, @Nullable Connection connection, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        super(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
        this.connection = connection;
    }

    public ConnectedQueryQueryConfigurationDelegate forQuery(QueryImpl query) {
        return new ConnectedQueryQueryConfigurationDelegate(query, this);
    }

    @Override
    public void close() {
        if (connection == null) return;
        try (var conn = connection) {
            //noinspection DataFlowIssue
            if (query.exceptions().isEmpty()) {
                if (atomic()) {
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }

    @Override
    public Connection connection() {
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(!atomic);
            } catch (SQLException e) {
                handleException(e);
                throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
            }
        }
        return connection;
    }
}
