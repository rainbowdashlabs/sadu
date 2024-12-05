/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.execution.writing;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.mapper.util.Results;
import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.execution.reading.Reader;
import de.chojo.sadu.queries.api.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;
import de.chojo.sadu.queries.call.CallImpl;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.exception.QueryException;
import de.chojo.sadu.queries.execution.reading.AutoMappedQuery;
import de.chojo.sadu.queries.execution.reading.MappedQuery;
import de.chojo.sadu.queries.query.ParsedQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.query.TokenizedQuery;
import de.chojo.sadu.queries.results.writing.insertion.InsertionResultImpl;
import de.chojo.sadu.queries.results.writing.manipulation.ManipulationResultImpl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public class CalledSingletonQueryImpl implements QueryProvider, CalledSingletonQuery {
    private final ParsedQueryImpl query;
    private final SingletonCall call;

    public CalledSingletonQueryImpl(ParsedQueryImpl query, SingletonCall call) {
        this.query = query;
        this.call = call;
    }

    @Override
    public <V> Reader<V> map(RowMapping<V> row) {
        return new MappedQuery<>(this, row);
    }

    @Override
    public <V> Reader<V> mapAs(Class<V> clazz) {
        return new AutoMappedQuery<>(this, clazz, MapperConfig.DEFAULT);
    }

    @Override
    public <V> Reader<V> mapAs(Class<V> clazz, MapperConfig config) {
        return new AutoMappedQuery<>(this, clazz, config);
    }

    @Override
    public InsertionResult insert() {
        return query.callConnection(() -> InsertionResultImpl.empty(this), conn -> {
            var changed = 0;
            //noinspection JDBCPrepareStatementWithNonConstantString
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                ((CallImpl) call.call()).apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                query().handleException(new QueryException(query, ex));
            }
            return new InsertionResultImpl(this, changed, Collections.emptyList());
        });
    }

    @Override
    public InsertionResult insertAndGetKeys() {
        return query.callConnection(() -> InsertionResultImpl.empty(this), conn -> {
            var changed = 0;
            //noinspection JDBCPrepareStatementWithNonConstantString
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql(), Statement.RETURN_GENERATED_KEYS)) {
                ((CallImpl) call.call()).apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
                return new InsertionResultImpl(this, changed, Results.generatedKeys(stmt));
            } catch (SQLException ex) {
                query().handleException(new QueryException(query, ex));
            }
            return InsertionResultImpl.empty(this);
        });
    }

    @Override
    public ManipulationResult update() {
        return query().callConnection(() -> ManipulationResultImpl.empty(this), conn -> {
            var changed = 0;
            //noinspection JDBCPrepareStatementWithNonConstantString
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                ((CallImpl) call.call()).apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                query().handleException(new QueryException(query, ex));
            }
            return new ManipulationResultImpl(this, changed);
        });
    }

    @Override
    public ManipulationResult delete() {
        return update();
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return call.call();
    }
}
