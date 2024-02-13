/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.execution.writing;

import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.execution.writing.CalledBatchQuery;
import de.chojo.sadu.queries.api.results.writing.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.call.CallImpl;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.query.ParsedQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.results.writing.ManipulationBatchQuery;
import de.chojo.sadu.queries.results.writing.ManipulationResultImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CalledBatchQueryImpl implements QueryProvider, CalledBatchQuery {
    private final ParsedQueryImpl parsedQuery;
    private final BatchCall calls;

    public CalledBatchQueryImpl(ParsedQueryImpl parsedQuery, BatchCall calls) {
        this.parsedQuery = parsedQuery;
        this.calls = calls;
    }

    @Override
    public ManipulationBatchResult insert() {
        return update();
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    @Override
    public ManipulationBatchResult update() {
        return query().callConnection(() -> new ManipulationBatchQuery(this, Collections.emptyList()), conn -> {
            var changed = new ArrayList<ManipulationResult>();
            for (var call : calls.calls()) {
                try (var stmt = conn.prepareStatement(parsedQuery.sql().tokenizedSql())) {
                    //TODO find way to return generated keys
                    ((CallImpl) call).apply(parsedQuery.sql(), stmt);
                    changed.add(new ManipulationResultImpl(this, stmt.executeUpdate()));
                } catch (SQLException ex) {
                    query().handleException(ex);
                }
            }
            return new ManipulationBatchQuery(this, changed);
        });
    }

    @Override
    public ManipulationBatchResult delete() {
        return update();
    }

    @Override
    public QueryImpl query() {
        return parsedQuery.query();
    }
}
