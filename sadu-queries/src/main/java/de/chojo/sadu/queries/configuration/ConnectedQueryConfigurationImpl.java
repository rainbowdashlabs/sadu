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

/**
 * A configuration that runs all its queries on one connection. The connection is taken from the data source on first
 * use or injected with {@link #withConnection(Connection)}. Before the first query, auto commit of the connection is
 * set to match {@link #atomic()}.
 */
public class ConnectedQueryConfigurationImpl extends ActiveQueryConfigurationImpl
        implements ConnectedQueryConfiguration {
    private Connection connection;
    private boolean prepared;

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
            if (atomic() && !conn.getAutoCommit()) {
                finishTransaction(conn);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            connection = null;
            prepared = false;
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
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
            }
            if (!prepared) {
                connection.setAutoCommit(!atomic);
                prepared = true;
            }
        } catch (SQLException e) {
            handleException(e);
            throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
        }
        return connection;
    }
}
