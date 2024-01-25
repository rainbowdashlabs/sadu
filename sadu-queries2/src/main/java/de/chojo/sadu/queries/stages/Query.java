/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.stages.base.ConnectionProvider;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.storage.ResultStorage;
import org.intellij.lang.annotations.Language;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Query implements DataSourceProvider, ConnectionProvider, QueryProvider {

    private final DataSource dataSource;
    private final ResultStorage storage = new ResultStorage();
    private Connection connection;

    private Query(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ParsedQuery query(@Language("sql") String sql, DataSource dataSource) {
        return new ParsedQuery(new Query(dataSource), TokenizedQuery.create(sql));
    }

    public static ParsedQuery query(@Language("sql") String sql, Object... format) {
        // TODO: Find way to supply datasource
        return query(sql.formatted(format), (DataSource) null);
    }

    @Override
    public DataSource source() {
        return dataSource;
    }

    @Override
    public Connection connection() throws SQLException {
        if (connection == null) {
            connection = source().getConnection();
            // if atomic
            connection.setAutoCommit(false);
        }
        return connection;
    }

    @Override
    public Query query() {
        return this;
    }

    public ResultStorage storage() {
        return storage;
    }
}
