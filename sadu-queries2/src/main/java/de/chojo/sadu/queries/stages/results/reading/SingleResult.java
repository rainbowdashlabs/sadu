/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results.reading;

import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.reading.MappedQuery;

/**
 * Result of a {@link MappedQuery#one()} call
 *
 * @param <T> Type of returned object
 */
public class SingleResult<T> implements Result<T>, QueryProvider {
    private final QueryProvider query;
    private final T result;

    public SingleResult(QueryProvider query, T result) {
        this.query = query;
        this.result = result;
    }

    @Override
    public T result() {
        return result;
    }

    @Override
    public Query query() {
        return query.query();
    }
}
