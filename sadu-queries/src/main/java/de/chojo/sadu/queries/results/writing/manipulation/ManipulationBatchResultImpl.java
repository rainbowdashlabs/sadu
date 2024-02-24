/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.results.writing.manipulation;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;
import de.chojo.sadu.queries.execution.writing.CalledBatchQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;

import java.util.List;

/**
 * Result of a {@link CalledBatchQueryImpl}
 */
public class ManipulationBatchResultImpl<T extends ManipulationResult> implements QueryProvider, ManipulationBatchResult<T> {
    private final QueryProvider query;
    private final List<T> results;

    public ManipulationBatchResultImpl(QueryProvider query, List<T> results) {
        this.query = query;
        this.results = results;
    }

    @Override
    public List<T> results() {
        return results;
    }

    @Override
    public int rows() {
        return results.stream().mapToInt(ManipulationResult::rows).sum();
    }

    @Override
    public boolean changed() {
        return results.stream().anyMatch(ManipulationResult::changed);
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
