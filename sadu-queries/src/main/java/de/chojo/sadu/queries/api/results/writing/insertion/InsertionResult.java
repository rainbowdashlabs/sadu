/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing.insertion;

import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;

import java.util.List;

/**
 * The InsertionResult interface represents the result of an insertion operation.
 * It extends the ManipulationResult interface and adds the ability to retrieve the keys of the inserted rows.
 */
public interface InsertionResult extends ManipulationResult {
    /**
     * Retrieves the keys of the inserted rows.
     *
     * @return a List of Long representing the keys of the inserted rows
     */
    List<Long> keys();

}
