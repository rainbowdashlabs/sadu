/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results.writing;

import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.stages.QueryImpl;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQueryImpl;

/**
 * Result of a {@link CalledSingletonQueryImpl}.
 */
public class ManipulationResultImpl implements QueryProvider, ManipulationResult {
    private final QueryProvider query;
    private final int rows;

    public ManipulationResultImpl(QueryProvider query, int rows) {
        this.query = query;
        this.rows = rows;
    }

    /**
     * Amount of changed rows
     *
     * @return rows
     */
    @Override
    public int rows() {
        return rows;
    }

    /**
     * Checks whether at least one row was changed
     *
     * @return true if at least one row was changed
     */
    @Override
    public boolean changed() {
        return rows != 0;
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }
}
