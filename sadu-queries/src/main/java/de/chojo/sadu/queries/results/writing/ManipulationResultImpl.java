/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.results.writing;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.execution.writing.CalledSingletonQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;

import java.util.List;

/**
 * Result of a {@link CalledSingletonQueryImpl}.
 */
public class ManipulationResultImpl implements QueryProvider, ManipulationResult {
    private final QueryProvider query;
    private final int rows;

    public ManipulationResultImpl(QueryProvider query, int rows) {
        this.query = query;
        this.rows = rows;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public boolean changed() {
        return rows != 0;
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }

    @Override
    public List<Exception> exceptions() {
        return query().exceptions();
    }
}
