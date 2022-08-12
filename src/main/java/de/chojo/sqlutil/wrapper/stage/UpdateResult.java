/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.wrapper.stage;

public class UpdateResult {
    private final int rows;

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
