/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing.manipulation;

import java.util.List;

/**
 * The ManipulationBatchResult interface represents the result of a batch manipulation operation.
 * It extends the ManipulationResult interface and provides methods to retrieve individual results of each manipulation operation.
 *
 * @param <T> the type of ManipulationResult
 */
public interface ManipulationBatchResult<T extends ManipulationResult> extends ManipulationResult {
    /**
     * Returns a list of ManipulationResult representing the results of each indivudual manipulation operation.
     *
     * @return The list of ManipulationResults.
     */
    List<T> results();

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
