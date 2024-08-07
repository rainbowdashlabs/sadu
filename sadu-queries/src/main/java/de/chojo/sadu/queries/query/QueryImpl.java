/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.query;

import de.chojo.sadu.core.base.DataSourceProvider;
import de.chojo.sadu.core.exceptions.ThrowingFunction;
import de.chojo.sadu.queries.api.base.ConnectionProvider;
import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.api.exception.ExceptionHolder;
import de.chojo.sadu.queries.api.query.Query;
import de.chojo.sadu.queries.api.configuration.ActiveQueryConfiguration;
import de.chojo.sadu.queries.api.configuration.ConnectedQueryConfiguration;
import de.chojo.sadu.queries.configuration.context.SimpleQueryContext;
import de.chojo.sadu.queries.storage.ResultStorageImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class QueryImpl implements DataSourceProvider, ConnectionProvider, QueryProvider, Query, ExceptionHolder {

    private final ActiveQueryConfiguration conf;
    private final ResultStorageImpl storage = new ResultStorageImpl();
    private final List<Exception> exceptions = new ArrayList<>();

    public QueryImpl(QueryConfiguration conf) {
        this.conf = conf.forQuery(new SimpleQueryContext(this));
    }

    @Override
    public DataSource source() {
        return conf.dataSource();
    }


    @Override
    public QueryImpl query() {
        return this;
    }

    public ResultStorageImpl storage() {
        return storage;
    }

    @Override
    public void logException(Exception ex) {
        exceptions.add(ex);
    }

    @Override
    public <T> T callConnection(Supplier<T> defaultResult, ThrowingFunction<T, Connection, SQLException> connectionConsumer) {
        if (conf instanceof ConnectedQueryConfiguration conn) {
            try {
                return connectionConsumer.apply(conn.connection());
            } catch (SQLException e) {
                conf.handleException(e);
            }
        } else {
            try (var conn = conf.dataSource().getConnection()) {
                conn.setAutoCommit(false);
                var result = connectionConsumer.apply(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conf.handleException(e);
            }
        }
        return defaultResult.get();
    }

    @Override
    public List<Exception> exceptions() {
        return exceptions;
    }

    public void handleException(SQLException e) {
        conf.handleException(e);
    }

    public QueryConfiguration configuration() {
        return conf;
    }
}
