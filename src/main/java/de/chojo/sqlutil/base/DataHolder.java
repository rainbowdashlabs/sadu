/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.base;

import de.chojo.sqlutil.exceptions.ExceptionTransformer;
import de.chojo.sqlutil.logging.LoggerAdapter;
import de.chojo.sqlutil.wrapper.QueryBuilder;
import de.chojo.sqlutil.wrapper.QueryBuilderFactory;
import de.chojo.sqlutil.wrapper.stage.ConfigurationStage;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;
import java.util.logging.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Base class which can be used for classes which call the database.
 * <p>
 * Provides convinience methods for connection retrieval, logging and a querybuilder.
 * <p>
 * You may use a {@link QueryBuilderFactory} for builder creation or extend {@link QueryFactoryHolder}
 */
public abstract class DataHolder implements DataSource {
    private static LoggerAdapter log = LoggerAdapter.wrap(getLogger(DataHolder.class));
    private final DataSource dataSource;

    /**
     * Create a new DataHolder
     *
     * @param dataSource data source
     */
    public DataHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Setup the logger used for logging.
     * @param adapter logger adapter
     */
    public static void setupLogger(LoggerAdapter adapter) {
        log = adapter;
    }

    /**
     * Get a query builder for easy sql execution.
     *
     * @param clazz clazz which should be retrieved. Doesnt matter if you want a list and multiple results or not.
     * @param <T>   type of result
     * @return query builder in a query stage
     */
    protected <T> ConfigurationStage<T> queryBuilder(Class<T> clazz) {
        return QueryBuilder.builder(dataSource, clazz);
    }

    /**
     * Logs the exception with a message
     *
     * @param message message
     * @param e       exception
     */
    public void logDbError(String message, SQLException e) {
        log.error(ExceptionTransformer.prettyException(message, e), e);
    }

    /**
     * Logs the exception with a default message.
     *
     * @param e exception
     */
    public void logDbError(SQLException e) {
        logDbError("An error occured while executing a query", e);
    }

    /**
     * Attempts to establish a connection with the data source that this DataSource object represents.
     *
     * @return a connection to the data source
     * @throws SQLException        if a database access error occurs
     * @throws SQLTimeoutException when the driver has determined that the timeout value specified by the setLoginTimeout method has been exceeded and has at least tried to cancel the current database connection attempt
     */
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }
}
