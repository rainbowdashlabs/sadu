/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.base;

import de.chojo.sadu.exceptions.ExceptionTransformer;
import de.chojo.sadu.logging.LoggerAdapter;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.stage.ConfigurationStage;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Base class which can be used for classes which call the database.
 * <p>
 * Provides convinience methods for connection retrieval, logging and a querybuilder.
 * <p>
 * You may use a {@link QueryFactory} for builder creation or extend {@link QueryFactory}
 */
public abstract class DataHolder implements DataSourceProvider {
    private static LoggerAdapter log = LoggerAdapter.wrap(LoggerFactory.getLogger(DataHolder.class));
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
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public DataSource source() {
        return dataSource;
    }
}
