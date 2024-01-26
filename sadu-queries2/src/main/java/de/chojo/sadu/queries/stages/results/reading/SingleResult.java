/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results.reading;

import de.chojo.sadu.queries.api.results.reading.Result;
import de.chojo.sadu.queries.stages.QueryImpl;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.reading.MappedQuery;

/**
 * Result of a {@link MappedQuery#first()} call
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
    public QueryImpl query() {
        return query.query();
    }
}
