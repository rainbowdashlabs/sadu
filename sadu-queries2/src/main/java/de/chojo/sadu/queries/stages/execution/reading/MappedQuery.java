/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.reading;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MappedQuery<V> extends QueryReader<V> {
    private final ThrowingFunction<V, Row, SQLException> mapper;

    public MappedQuery(CalledSingletonQuery query, ThrowingFunction<V, Row, SQLException> mapper) {
        super(query);
        this.mapper = mapper;
    }

    @Override
    protected RowMapping<V> mapper(ResultSet set) {
        return RowMapping.create(mapper);
    }
}
