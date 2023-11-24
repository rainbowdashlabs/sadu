/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.mapped;

import java.util.List;

public class ManipulationBatchQuery {
    private final List<ManipulationQuery> results;

    public ManipulationBatchQuery(List<ManipulationQuery> results) {
        this.results = results;
    }

    /**
     * List of results of all calls in the batch
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
}
