/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing;

import java.util.List;

public interface ManipulationBatchResult extends ManipulationResult {
    /**
     * Returns a list of ManipulationResult representing the results of each indivudual manipulation operation.
     *
     * @return The list of ManipulationResults.
     */
    List<? extends ManipulationResult> results();

    /**
     * Retrieves the number of rows affected by the all executions of the query.
     *
     * @return The number of rows affected by the batch execution.
     */
    @Override
    int rows();

    /**
     * Determines whether there were any changes made by the query execution.
     *
     * @return {@code true} if changes were made, {@code false} otherwise.
     */
    @Override
    boolean changed();
}
