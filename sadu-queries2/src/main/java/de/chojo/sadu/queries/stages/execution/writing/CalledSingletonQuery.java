/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.writing;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.ParsedQuery;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.execution.reading.AutoMappedQuery;
import de.chojo.sadu.queries.stages.execution.reading.MappedQuery;
import de.chojo.sadu.queries.stages.execution.reading.QueryReader;
import de.chojo.sadu.queries.stages.results.writing.ManipulationQuery;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;

public class CalledSingletonQuery implements QueryProvider {
    private final ParsedQuery query;
    private final SingletonCall call;

    public CalledSingletonQuery(ParsedQuery query, SingletonCall call) {
        this.query = query;
        this.call = call;
    }

    public <V> QueryReader<V> map(ThrowingFunction<V, Row, SQLException> mapper) {
        return new MappedQuery<>(this, mapper);
    }

    public <V> QueryReader<V> mapAs(Class<V> clazz) {
        return new AutoMappedQuery<>(this, clazz, MapperConfig.DEFAULT);
    }

    public <V> QueryReader<V> mapAs(Class<V> clazz, MapperConfig config) {
        return new AutoMappedQuery<>(this, clazz, config);
    }

    public ManipulationQuery insert() {
        return query.callConnection(() -> new ManipulationQuery(this, 0), conn -> {
            var changed = 0;
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                //TODO find way to return generated keys
                call.call().apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                query().handleException(ex);
            }
            return new ManipulationQuery(this, changed);
        });
    }

    public ManipulationQuery update() {
        return query().callConnection(() -> new ManipulationQuery(this, 0), conn -> {
            var changed = 0;
            try (var stmt = conn.prepareStatement(query.sql().tokenizedSql())) {
                call.call().apply(query.sql(), stmt);
                changed = stmt.executeUpdate();
            } catch (SQLException ex) {
                // TODO: logging
            }
            return new ManipulationQuery(this, changed);
        });
    }

    public ManipulationQuery delete() {
        return update();
    }

    @Override
    public Query query() {
        return query.query();
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return call.call();
    }
}
