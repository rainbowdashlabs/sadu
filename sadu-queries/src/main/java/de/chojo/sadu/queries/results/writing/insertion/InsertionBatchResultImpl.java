/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.results.writing.insertion;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionBatchResult;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.results.writing.manipulation.ManipulationBatchResultImpl;

import java.util.List;

public class InsertionBatchResultImpl extends ManipulationBatchResultImpl<InsertionResult> implements InsertionBatchResult<InsertionResult> {
    public InsertionBatchResultImpl(QueryProvider query, List<InsertionResult> results) {
        super(query, results);
    }

    @Override
    public List<Long> keys() {
        return results().stream().flatMap(r -> r.keys().stream()).toList();
    }
}
