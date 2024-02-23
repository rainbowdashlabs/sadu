/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.results.writing;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.execution.writing.CalledBatchQueryImpl;

import java.util.List;

/**
 * Result of a {@link CalledBatchQueryImpl}
 */
public class ManipulationBatchQuery implements QueryProvider, ManipulationBatchResult {
    private final QueryProvider query;
    private final List<ManipulationResult> results;

    public ManipulationBatchQuery(QueryProvider query, List<ManipulationResult> results) {
        this.query = query;
        this.results = results;
    }

    @Override
    public List<ManipulationResult> results() {
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
