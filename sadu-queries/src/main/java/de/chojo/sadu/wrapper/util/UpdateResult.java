/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.util;

import de.chojo.sadu.wrapper.stage.InsertStage;
import de.chojo.sadu.wrapper.stage.UpdateStage;

/**
 * Represent the result of an {@link UpdateStage} or {@link InsertStage}
 */
public class UpdateResult {
    private final int rows;

    /**
     * Creates a new update result
     *
     * @param rows changed rows
     */
    public UpdateResult(int rows) {
        this.rows = rows;
    }

    /**
     * The amount of affected rows.
     *
     * @return changed rows
     */
    public int rows() {
        return rows;
    }

    /**
     * Checks if something has changed.
     *
     * @return true if something has changed
     */
    public boolean changed() {
        return rows > 0;
    }
}
