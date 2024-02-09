/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results.writing;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.stages.execution.writing.CalledBatchQueryImpl;

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

    /**
     * List of results of all calls in the batch
     *
     * @return list of results
     */
    @Override
    public List<ManipulationResult> results() {
        return results;
    }

    /**
     * total amount of changed rows
     *
     * @return total count
     */
    @Override
    public int rows() {
        return results.stream().mapToInt(ManipulationResult::rows).sum();
    }

    /**
     * Checks whether at least one row was changed.
     *
     * @return true if one row or more were changed
     */
    @Override
    public boolean changed() {
        return results.stream().anyMatch(ManipulationResult::changed);
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }
}
