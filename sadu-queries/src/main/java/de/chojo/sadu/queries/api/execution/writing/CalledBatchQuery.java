/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.execution.writing;

import de.chojo.sadu.queries.api.results.writing.insertion.InsertionBatchResult;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;

/**
 * The CalledBatchQuery interface represents a batch query that can perform insert, update, and delete operations
 * on a data source.
 */
public interface CalledBatchQuery {
    /**
     * Inserts the specified values into the table.
     *
     * @return The {@link InsertionBatchResult} that represents the results of the insert operations.
     */
    InsertionBatchResult<InsertionResult> insert();

    /**
     * Inserts the specified values into the table.
     * <p>
     * Additionally, generated keys will be retrieved.
     *
     * @return The {@link InsertionBatchResult} that represents the results of the insert operations.
     */
    InsertionBatchResult<InsertionResult> insertAndGetKeys();


    /**
     * Executes update operations as part of a batch query.
     * Returns the result of the update operation.
     *
     * @return The {@link ManipulationBatchResult} that represents the results of the update operations.
     */
    ManipulationBatchResult<ManipulationResult> update();

    /**
     * Deletes the selected items or records from the data source.
     *
     * @return The {@link ManipulationBatchResult} that represents the results of the delete operations.
     */
    ManipulationBatchResult<ManipulationResult> delete();
}
