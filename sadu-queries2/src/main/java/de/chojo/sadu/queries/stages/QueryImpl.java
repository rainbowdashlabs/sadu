/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.queries.api.Query;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.stages.base.ConnectionProvider;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.storage.ResultStorage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class QueryImpl implements DataSourceProvider, ConnectionProvider, QueryProvider, Query {

    private final QueryConfiguration conf;
    private final ResultStorage storage = new ResultStorage();
    private final List<Exception> exceptions = new ArrayList<>();

    public QueryImpl(QueryConfiguration conf) {
        this.conf = conf.forQuery(this);
    }

    @Override
    public DataSource source() {
        return conf.dataSource();
    }


    @Override
    public QueryImpl query() {
        return this;
    }

    public ResultStorage storage() {
        return storage;
    }

    public void logException(Exception ex) {
        exceptions.add(ex);
    }

    @Override
    public <T> T callConnection(Supplier<T> defaultResult, ThrowingFunction<T, Connection, SQLException> connectionConsumer) {
        if (!conf.hasConnection()) {
            try (var conn = conf.dataSource().getConnection()) {
                conn.setAutoCommit(false);
                var result = connectionConsumer.apply(conn);
                conn.commit();
                return result;
            } catch (SQLException e) {
                conf.handleException(e);
            }
        } else {
            try {
                return connectionConsumer.apply(conf.connection());
            } catch (SQLException e) {
                conf.handleException(e);
            }
        }
        return defaultResult.get();
    }

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
