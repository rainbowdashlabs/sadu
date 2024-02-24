/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.execution.writing;

import de.chojo.sadu.mapper.util.Results;
import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.execution.writing.CalledBatchQuery;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionBatchResult;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;
import de.chojo.sadu.queries.call.CallImpl;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.query.ParsedQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.results.writing.insertion.InsertionBatchResultImpl;
import de.chojo.sadu.queries.results.writing.insertion.InsertionResultImpl;
import de.chojo.sadu.queries.results.writing.manipulation.ManipulationBatchResultImpl;
import de.chojo.sadu.queries.results.writing.manipulation.ManipulationResultImpl;

import java.sql.SQLException;
import java.sql.Statement;
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
    public InsertionBatchResult<InsertionResult> insertAndGetKeys() {
        return query().callConnection(() -> new InsertionBatchResultImpl(this, Collections.emptyList()), conn -> {
            var changed = new ArrayList<InsertionResult>();
            for (var call : calls.calls()) {
                try (var stmt = conn.prepareStatement(parsedQuery.sql().tokenizedSql(), Statement.RETURN_GENERATED_KEYS)) {
                    ((CallImpl) call).apply(parsedQuery.sql(), stmt);
                    var changes = stmt.executeUpdate();
                    changed.add(new InsertionResultImpl(this, changes, Results.generatedKeys(stmt)));
                } catch (SQLException ex) {
                    query().handleException(ex);
                }
            }
            return new InsertionBatchResultImpl(this, changed);
        });
    }

    @Override
    public InsertionBatchResult<InsertionResult> insert() {
        return query().callConnection(() -> new InsertionBatchResultImpl(this, Collections.emptyList()), conn -> {
            var changed = new ArrayList<InsertionResult>();
            for (var call : calls.calls()) {
                try (var stmt = conn.prepareStatement(parsedQuery.sql().tokenizedSql())) {
                    ((CallImpl) call).apply(parsedQuery.sql(), stmt);
                    changed.add(new InsertionResultImpl(this, stmt.executeUpdate(), Collections.emptyList()));
                } catch (SQLException ex) {
                    query().handleException(ex);
                }
            }
            return new InsertionBatchResultImpl(this, changed);
        });
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    @Override
    public ManipulationBatchResult<ManipulationResult> update() {
        return query().callConnection(() -> new ManipulationBatchResultImpl<>(this, Collections.emptyList()), conn -> {
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
            return new ManipulationBatchResultImpl<>(this, changed);
        });
    }

    @Override
    public ManipulationBatchResult<ManipulationResult> delete() {
        return update();
    }

    @Override
    public QueryImpl query() {
        return parsedQuery.query();
    }
}
