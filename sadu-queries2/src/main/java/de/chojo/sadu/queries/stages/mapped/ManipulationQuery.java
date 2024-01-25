/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.mapped;

import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;

public class ManipulationQuery implements QueryProvider {
    private final QueryProvider query;
    private final int rows;

    public ManipulationQuery(QueryProvider query, int rows) {
        this.query = query;
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

    @Override
    public Query query() {
        return query.query();
    }
}
