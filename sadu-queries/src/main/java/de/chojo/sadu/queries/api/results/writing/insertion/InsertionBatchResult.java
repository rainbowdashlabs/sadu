/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing.insertion;

import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationBatchResult;

import java.util.List;

/**
 * The InsertionBatchResult interface represents the result of a batch insertion operation.
 * It extends the ManipulationBatchResult interface and adds the ability to retrieve the keys of the inserted rows.
 *
 * @param <T> the type of InsertionResult
 */
public interface InsertionBatchResult<T extends InsertionResult> extends ManipulationBatchResult<T>, InsertionResult {
    /**
     * Retrieves the keys of the inserted rows of all executed statements.
     *
     * @return a List of Long representing the keys of the inserted rows
     */
    @Override
    List<Long> keys();
}
