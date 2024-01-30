/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.query;

import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.query.ParsedQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;
import org.intellij.lang.annotations.Language;

/**
 * Starting point for executing one or more queries.
 */
public interface Query {
    /**
     * Create a new query.
     *
     * @param configuration query configuration
     * @param sql           the query you want to execute
     * @param format        will work like calling {@link String#formatted(Object...)}.
     *                      <b>DO NOT USE THAT FOR PARAMETER OR WITH USER INPUT</b>
     * @return a parsed query
     */
    static ParsedQuery query(QueryConfiguration configuration, @Language("sql") String sql, Object... format) {
        return ParsedQueryImpl.create(new QueryImpl(configuration), sql, format);
    }

    /**
     * Create a new query using the default query configuration.
     * <p>
     * {@link QueryConfiguration#setDefault(QueryConfiguration)} has to be used before to configure it.
     *
     * @param sql    the query you want to execute
     * @param format will work like calling {@link String#formatted(Object...)}.
     *               <b>DO NOT USE THAT FOR PARAMETER OR WITH USER INPUT</b>
     * @return a parsed query
     */
    static ParsedQuery query(@Language("sql") String sql, Object... format) {
        return ParsedQueryImpl.create(new QueryImpl(QueryConfiguration.getDefault()), sql, format);
    }
}
