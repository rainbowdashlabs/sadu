/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.mapped;

public class ManipulationQuery {
    private final int rows;

    public ManipulationQuery(int rows) {
        this.rows = rows;
    }

    /**
     * Amount of changed rows
     *
     * @return rows
     */
    public int rows() {
        return rows;
    }

    /**
     * Checks whether at least one row was changed
     *
     * @return true if at least one row was changed
     */
    public boolean changed() {
        return rows != 0;
    }
}
