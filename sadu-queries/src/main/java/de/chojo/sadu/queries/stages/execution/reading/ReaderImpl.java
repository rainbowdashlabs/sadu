/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.reading;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.api.base.QueryProvider;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.execution.reading.Reader;
import de.chojo.sadu.queries.api.query.AppendedQuery;
import de.chojo.sadu.queries.api.results.reading.Result;
import de.chojo.sadu.queries.call.CallImpl;
import de.chojo.sadu.queries.query.AppendedQueryImpl;
import de.chojo.sadu.queries.query.QueryImpl;
import de.chojo.sadu.queries.query.TokenizedQuery;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQueryImpl;
import de.chojo.sadu.queries.stages.results.reading.MultiResult;
import de.chojo.sadu.queries.stages.results.reading.SingleResult;
import de.chojo.sadu.mapper.wrapper.Row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ReaderImpl<V> implements QueryProvider, Reader<V> {
    private final CalledSingletonQueryImpl query;

    public ReaderImpl(CalledSingletonQueryImpl query) {
        this.query = query;
    }

    @Override
    public Result<V> firstResult() {
        return mapOne();
    }

    @Override
    public Result<List<V>> allResults() {
        return mapAll();
    }

    @Override
    public AppendedQuery storeOneAndAppend(String key) {
        store(key, mapOne());
        return new AppendedQueryImpl(this);
    }

    @Override
    public AppendedQuery storeAllAndAppend(String key) {
        store(key, mapAll());
        return new AppendedQueryImpl(this);
    }

    private <T extends Result<?>> void store(String key, T result) {
        query.query().storage().store(key, result);
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    private SingleResult<V> mapOne() {
        return query().callConnection(() -> new SingleResult<>(this, null), conn -> {
            try (var stmt = conn.prepareStatement(sql().tokenizedSql())) {
                ((CallImpl) call()).apply(sql(), stmt);
                var resultSet = stmt.executeQuery();
                var row = new Row(resultSet, mapperConfig());
                if (resultSet.next()) {
                    return new SingleResult<>(this, mapper(resultSet).map(row));
                }
            }
            return new SingleResult<>(this, null);
        });
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    private MultiResult<List<V>> mapAll() {
        return query().callConnection(() -> new MultiResult<>(this, Collections.emptyList()), conn -> {
            var result = new ArrayList<V>();
            try (var stmt = conn.prepareStatement(sql().tokenizedSql())) {
                ((CallImpl) call()).apply(sql(), stmt);
                var resultSet = stmt.executeQuery();
                var row = new Row(resultSet, mapperConfig());
                while (resultSet.next()) result.add(mapper(resultSet).map(row));
            }
            return new MultiResult<>(this, result);
        });
    }

    protected abstract RowMapping<V> mapper(ResultSet set) throws SQLException;

    protected MapperConfig mapperConfig() {
        return MapperConfig.DEFAULT;
    }

    @Override
    public Optional<V> first() {
        return Optional.ofNullable(firstResult().result());
    }

    @Override
    public List<V> all() {
        return Objects.requireNonNullElse(allResults().result(), Collections.emptyList());
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return query.call();
    }

    @Override
    public QueryImpl query() {
        return query.query();
    }
}
