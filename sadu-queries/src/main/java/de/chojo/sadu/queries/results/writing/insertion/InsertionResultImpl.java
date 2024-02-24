/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.results.writing.insertion;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.results.writing.manipulation.ManipulationResultImpl;

import java.util.Collections;
import java.util.List;

public class InsertionResultImpl extends ManipulationResultImpl implements InsertionResult {
    private final List<Long> keys;

    public InsertionResultImpl(QueryProvider query, int rows, List<Long> keys) {
        super(query, rows);
        this.keys = keys;
    }

    public static InsertionResultImpl empty(QueryProvider query) {
        return new InsertionResultImpl(query, 0, Collections.emptyList());
    }

    @Override
    public List<Long> keys() {
        return keys;
    }
}
