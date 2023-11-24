/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.stages.base.ConnectionProvider;
import org.intellij.lang.annotations.Language;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Query<T> implements DataSourceProvider, ConnectionProvider {

    private final DataSource dataSource;
    private Connection connection;

    private Query(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ParsedQuery<?> query(@Language("sql") String sql, DataSource dataSource) {
        return new ParsedQuery<>(new Query<>(dataSource), TokenizedQuery.create(sql));
    }

    public static ParsedQuery<?> query(@Language("sql") String sql, Object... format) {
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
}
