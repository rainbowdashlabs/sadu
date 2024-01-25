/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.mapped;

import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;

import java.util.List;

public class ManipulationBatchQuery implements QueryProvider {
    private final QueryProvider query;
    private final List<ManipulationQuery> results;

    public ManipulationBatchQuery(QueryProvider query, List<ManipulationQuery> results) {
        this.query = query;
        this.results = results;
    }

    /**
     * List of results of all calls in the batch
     *
     * @return list of results
     */
    public List<ManipulationQuery> results() {
        return results;
    }

    /**
     * total amount of changed rows
     *
     * @return total count
     */
    public int totalRows() {
        return results.stream().mapToInt(ManipulationQuery::rows).sum();
    }

    /**
     * Checks whether at least one row was changed.
     *
     * @return true if one row or more were changed
     */
    public boolean changed() {
        return results.stream().anyMatch(ManipulationQuery::changed);
    }

    @Override
    public Query query() {
        return query.query();
    }
}
