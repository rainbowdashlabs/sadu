/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.reading;

import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.stages.execution.writing.CalledSingletonQueryImpl;

import java.sql.ResultSet;

public class MappedQuery<V> extends ReaderImpl<V> {
    private final RowMapping<V> mapper;

    public MappedQuery(CalledSingletonQueryImpl query, RowMapping<V> mapper) {
        super(query);
        this.mapper = mapper;
    }

    @Override
    protected RowMapping<V> mapper(ResultSet set) {
        return mapper;
    }
}
