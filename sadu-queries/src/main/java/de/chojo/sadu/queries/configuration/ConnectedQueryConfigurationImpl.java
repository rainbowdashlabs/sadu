/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.ConnectedQueryConfiguration;
import de.chojo.sadu.queries.api.configuration.context.QueryContext;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import javax.sql.DataSource;

public class ConnectedQueryConfigurationImpl extends ActiveQueryConfigurationImpl
        implements ConnectedQueryConfiguration {
    private Connection connection;

    ConnectedQueryConfigurationImpl(
            QueryContext context,
            DataSource dataSource,
            @Nullable Connection connection,
            boolean atomic,
            boolean throwExceptions,
            Consumer<SQLException> exceptionHandler,
            RowMapperRegistry rowMapperRegistry) {
        super(dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry, context);
        this.connection = connection;
    }

    @Override
    public ConnectedQueryQueryConfigurationDelegate forQuery(QueryContext context) {
        // TODO: Maybe store the different query instances from the context in the future.
        return new ConnectedQueryQueryConfigurationDelegate(this);
    }

    @Override
    public ConnectedQueryConfiguration withConnection(Connection connection) {
        close();
        this.connection = connection;
        return this;
    }

    @Override
    public void close() {
        if (connection == null) return;
        try (var conn = connection) {
            if (atomic()) {
                finishTransaction(conn);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            connection = null;
        }
    }

    private void finishTransaction(Connection conn) throws SQLException {
        if (context.exceptions().isEmpty()) {
            conn.commit();
        } else {
            conn.rollback();
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
