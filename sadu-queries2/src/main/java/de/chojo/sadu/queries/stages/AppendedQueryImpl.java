/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.queries.api.query.AppendedQuery;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import org.intellij.lang.annotations.Language;

public class AppendedQueryImpl implements QueryProvider, AppendedQuery {
    private final QueryProvider query;

    public AppendedQueryImpl(QueryProvider query) {
        this.query = query;
    }


    @Override
    public ParsedQueryImpl query(@Language("sql") String sql, Object... format) {
        return ParsedQueryImpl.create(query, sql.formatted(format));
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }
}
