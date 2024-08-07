/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.query.QueryImpl;
import org.intellij.lang.annotations.Language;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public interface QueryConfiguration {
    AtomicReference<QueryConfiguration> DEFAULT = new AtomicReference<>(null);
    /**
     * Retrieves the default QueryConfiguration.
     *
     * @return the default QueryConfiguration
     */
    static QueryConfiguration getDefault() {
        return Objects.requireNonNull(DEFAULT.get(), "You need to configure the configuration first by calling QueryConfiguration.setDefault()");
    }

    /**
     * Sets the default query configuration.
     *
     * @param configuration the query configuration to set as default
     */
    static void setDefault(QueryConfiguration configuration) {
        DEFAULT.set(configuration);
    }

    /**
     * Creates a new QueryConfigurationBuilder instance with the given DataSource.
     *
     * @param source the DataSource to use for QueryConfiguration
     * @return a QueryConfigurationBuilder instance
     */
    static QueryConfigurationBuilder builder(DataSource source) {
        return new QueryConfigurationBuilder(source);
    }

    QueryConfiguration forQuery(QueryImpl query);

    void handleException(SQLException e);

    boolean atomic();

    boolean throwExceptions();

    RowMapperRegistry rowMapperRegistry();

    DataSource dataSource();

    ParsedQuery query(@Language("sql") String sql, Object... format);

    ConnectedQueryConfigurationImpl withSingleTransaction();
}
