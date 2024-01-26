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
 * Result of a {@link MappedQuery#all()} call
 *
 * @param <T> Type of returned object
 */

public class MultiResult<T> implements QueryProvider, Result<T> {
    private final QueryProvider query;
    private final T results;

    public MultiResult(QueryProvider query, T results) {
        this.query = query;
        this.results = results;
    }

    @Override
    public Query query() {
        return query.query();
    }

    @Override
    public T result() {
        return results;
    }
}
