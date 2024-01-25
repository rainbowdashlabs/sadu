/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages;

import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.calls.CallSupplier;
import de.chojo.sadu.queries.stages.parsed.CalledBatchQuery;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;

public class ParsedQuery implements QueryProvider {
    private final QueryProvider query;
    private final TokenizedQuery sql;

    protected ParsedQuery(QueryProvider query, TokenizedQuery sql) {
        this.query = query;
        this.sql = sql;
    }

    public CalledBatchQuery batch(BatchCall param) {
        return new CalledBatchQuery(this, param);
    }

    public CalledSingletonQuery single(SingletonCall param) {
        return new CalledSingletonQuery(this, param);
    }

    // TODO: Solve signature clash
    public CalledBatchQuery batch(CallSupplier<BatchCall> param) {
        return new CalledBatchQuery(this, param.supply(query().storage()));
    }

    public CalledSingletonQuery single(CallSupplier<SingletonCall> param) {
        return new CalledSingletonQuery(this, param.supply(query().storage()));
    }

    public TokenizedQuery sql() {
        return sql;
    }

    public Query query() {
        return query.query();
    }
}
