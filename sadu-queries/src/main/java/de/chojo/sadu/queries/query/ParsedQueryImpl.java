/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.query;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.execution.writing.CalledBatchQuery;
import de.chojo.sadu.queries.api.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.queries.api.query.ParsedQuery;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.CallSupplier;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.execution.writing.CalledBatchQueryImpl;
import de.chojo.sadu.queries.execution.writing.CalledSingletonQueryImpl;

public class ParsedQueryImpl implements QueryProvider, ParsedQuery {
    private final QueryProvider query;
    private final TokenizedQuery sql;

    private ParsedQueryImpl(QueryProvider query, TokenizedQuery sql) {
        this.query = query;
        this.sql = sql;
    }

    public static ParsedQueryImpl create(QueryProvider query, String sql, Object... format) {
        return new ParsedQueryImpl(query, TokenizedQuery.create(sql.formatted(format)));
    }

    @Override
    public CalledSingletonQuery single(SingletonCall param) {
        return new CalledSingletonQueryImpl(this, param);
    }

    @Override
    public CalledSingletonQuery single(CallSupplier<SingletonCall> call) {
        return new CalledSingletonQueryImpl(this, call.supply(query().storage()));
    }

    @Override
    public CalledBatchQuery batch(BatchCall calls) {
        return new CalledBatchQueryImpl(this, calls);
    }

    @Override
    public CalledBatchQuery batch(CallSupplier<BatchCall> calls) {
        return new CalledBatchQueryImpl(this, calls.supply(query().storage()));
    }

    public TokenizedQuery sql() {
        return sql;
    }

    public QueryImpl query() {
        return query.query();
    }
}
