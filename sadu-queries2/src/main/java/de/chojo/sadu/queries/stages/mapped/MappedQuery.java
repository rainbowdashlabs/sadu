/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.mapped;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.stages.AppendedQuery;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;
import de.chojo.sadu.queries.stages.results.MultiResult;
import de.chojo.sadu.queries.stages.results.Result;
import de.chojo.sadu.queries.stages.results.SingleResult;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MappedQuery<V> implements QueryProvider {
    private final CalledSingletonQuery query;
    private final ThrowingFunction<V, Row, SQLException> mapper;
    private String key;

    public MappedQuery(CalledSingletonQuery query, ThrowingFunction<V, Row, SQLException> mapper) {
        this.query = query;
        this.mapper = mapper;
    }

    // TODO: Mapping
    public SingleResult<V> one() {
        return mapOne();
    }

    // TODO: Mapping
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

    private SingleResult<V> mapOne() {
        try (var stmt = connection().prepareStatement(sql().tokenizedSql())) {
            call().apply(sql(), stmt);
            ResultSet resultSet = stmt.executeQuery();
            // TODO: Get mapper config in here.
            var row = new Row(resultSet, MapperConfig.DEFAULT);
            if (resultSet.next()) {
                return new SingleResult<>(this, mapper.apply(row));
            }
        } catch (SQLException e) {
            // TODO: logging
        }
        return new SingleResult<>(this, null);
    }

    private MultiResult<List<V>> mapAll() {
        var result = new ArrayList<V>();
        try (var stmt = connection().prepareStatement(sql().tokenizedSql())) {
            call().apply(sql(), stmt);
            ResultSet resultSet = stmt.executeQuery();
            // TODO: Get mapper config in here.
            var row = new Row(resultSet, MapperConfig.DEFAULT);
            while (resultSet.next()) result.add(mapper.apply(row));
        } catch (SQLException e) {
            // TODO: logging
        }
        return new MultiResult<>(this, result);
    }

    public Optional<V> oneAndGet() {
        return Optional.ofNullable(one().result());
    }

    public List<V> allAndGet() {
        return Objects.requireNonNullElse(all().result(), Collections.emptyList());
    }

    public Connection connection() throws SQLException {
        return query.connection();
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
