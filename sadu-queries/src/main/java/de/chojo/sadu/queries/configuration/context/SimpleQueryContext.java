/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.configuration.context;

import de.chojo.sadu.queries.api.configuration.context.QueryContext;
import de.chojo.sadu.queries.query.QueryImpl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SimpleQueryContext implements QueryContext {
    private final List<Exception> exceptions = new LinkedList<>();
    private final QueryImpl query;

    public SimpleQueryContext(QueryImpl query) {
        this.query = query;
    }

    @Override
    public List<Exception> exceptions() {
        return Collections.unmodifiableList(exceptions);
    }

    @Override
    public void logException(Exception e) {
        exceptions.add(e);
    }
}
