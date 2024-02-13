/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing;

import de.chojo.sadu.queries.api.results.BaseResult;

/**
 * The ManipulationResult interface represents the result of a manipulation operation,
 * such as an insert, update, or delete operation.
 */
public interface ManipulationResult extends BaseResult {
    /**
     * Returns the total number of changed rows resulting from a manipulation operation.
     *
     * @return the total number of changed rows
     */
    int rows();

    /**
     * Checks whether at least one row was changed.
     *
     * @return true if one row or more were changed
     */
    boolean changed();
}
