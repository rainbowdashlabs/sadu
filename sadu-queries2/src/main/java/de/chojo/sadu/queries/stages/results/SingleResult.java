/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;

import javax.sql.DataSource;

public class SingleResult<T> implements DataSourceProvider, Result<T>, QueryProvider {
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
    public DataSource source() {
        return null;
    }

    @Override
    public Query query() {
        return query.query();
    }
}
