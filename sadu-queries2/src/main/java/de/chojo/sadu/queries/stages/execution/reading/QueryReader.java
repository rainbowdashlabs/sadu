/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.reading;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.stages.AppendedQuery;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.queries.stages.results.reading.MultiResult;
import de.chojo.sadu.queries.stages.results.reading.Result;
import de.chojo.sadu.queries.stages.results.reading.SingleResult;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class QueryReader<V> implements QueryProvider {
    private final CalledSingletonQuery query;

    public QueryReader(CalledSingletonQuery query) {
        this.query = query;
    }

    public SingleResult<V> one() {
        return mapOne();
    }

    public MultiResult<List<V>> all() {
        return mapAll();
    }

    public AppendedQuery storeOneAndAppend(String key) {
        store(key, mapOne());
        return new AppendedQuery(this);
    }

    public AppendedQuery storeAllAndAppend(String key) {
        store(key, mapAll());
        return new AppendedQuery(this);
    }

    private <T extends Result<?>> void store(String key, T result) {
        query.query().storage().store(key, result);
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    private SingleResult<V> mapOne() {
        return query().callConnection(() -> new SingleResult<>(this, null), conn -> {
            try (var stmt = conn.prepareStatement(sql().tokenizedSql())) {
                call().apply(sql(), stmt);
                ResultSet resultSet = stmt.executeQuery();
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
                call().apply(sql(), stmt);
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

    public Optional<V> oneAndGet() {
        return Optional.ofNullable(one().result());
    }

    public List<V> allAndGet() {
        return Objects.requireNonNullElse(all().result(), Collections.emptyList());
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return query.call();
    }

    @Override
    public Query query() {
        return query.query();
    }
}
