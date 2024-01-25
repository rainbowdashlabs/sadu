/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import org.intellij.lang.annotations.Language;

public class AppendedQuery implements QueryProvider {
    private final QueryProvider query;

    public AppendedQuery(QueryProvider query) {
        this.query = query;
    }

    public ParsedQuery query(@Language("sql") String sql) {
        return new ParsedQuery(query, TokenizedQuery.create(sql));
    }

    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        // TODO: Find way to supply datasource
        return query(sql.formatted(format));
    }

    @Override
    public Query query() {
        return query.query();
    }
}
