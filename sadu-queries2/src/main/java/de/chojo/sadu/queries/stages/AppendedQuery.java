/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.queries.stages.base.QueryProvider;
import org.intellij.lang.annotations.Language;

public class AppendedQuery implements QueryProvider {
    private final QueryProvider query;

    public AppendedQuery(QueryProvider query) {
        this.query = query;
    }


    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        return ParsedQuery.create(query, sql.formatted(format));
    }

    @Override
    public Query query() {
        return query.query();
    }
}
