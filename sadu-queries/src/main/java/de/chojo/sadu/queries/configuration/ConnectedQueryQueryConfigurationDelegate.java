/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.configuration.ConnectedQueryConfiguration;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.api.configuration.context.QueryContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectedQueryQueryConfigurationDelegate implements ConnectedQueryConfiguration {
    private final ConnectedQueryConfigurationImpl configuration;

    ConnectedQueryQueryConfigurationDelegate(ConnectedQueryConfigurationImpl configuration) {
        this.configuration = configuration;
    }

    @Override
    public Connection connection() {
        return configuration.connection();
    }

    @Override
    public void close() {
        configuration.close();
    }

    @Override
    public ConnectedQueryQueryConfigurationDelegate forQuery(QueryContext exceptionHolder) {
        return configuration.forQuery(exceptionHolder);
    }

    @Override
    public ParsedQuery query(String sql, Object... format) {
        return configuration.query(sql, format);
    }

    @Override
    public DataSource dataSource() {
        return configuration.dataSource();
    }

    @Override
    public RowMapperRegistry rowMapperRegistry() {
        return configuration.rowMapperRegistry();
    }

    @Override
    public boolean throwExceptions() {
        return configuration.throwExceptions();
    }

    @Override
    public boolean atomic() {
        return configuration.atomic();
    }

    @Override
    public void handleException(SQLException e) {
        configuration.handleException(e);
    }

    @Override
    public QueryContext context() {
        return configuration.context();
    }

    @Override
    public ConnectedQueryConfigurationImpl withSingleTransaction() {
        return configuration.withSingleTransaction();
    }
}
