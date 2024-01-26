/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.writing;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.api.execution.reading.Reader;
import de.chojo.sadu.queries.api.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.ParsedQueryImpl;
import de.chojo.sadu.queries.stages.QueryImpl;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.reading.AutoMappedQuery;
import de.chojo.sadu.queries.stages.execution.reading.MappedQuery;
import de.chojo.sadu.queries.stages.results.writing.ManipulationResultImpl;

import java.sql.SQLException;

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
    public ManipulationResult insert() {
        return query.callConnection(() -> new ManipulationResultImpl(this, 0), conn -> {
            var changed = 0;
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                //TODO find way to return generated keys
                call.call().apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                query().handleException(ex);
            }
            return new ManipulationResultImpl(this, changed);
        });
    }

    @Override
    public ManipulationResult update() {
        return query().callConnection(() -> new ManipulationResultImpl(this, 0), conn -> {
            var changed = 0;
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                call.call().apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                query().logException(ex);
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
