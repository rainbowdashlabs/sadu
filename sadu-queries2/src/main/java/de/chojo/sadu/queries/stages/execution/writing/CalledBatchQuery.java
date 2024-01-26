/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.execution.writing;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.stages.ParsedQuery;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.base.QueryProvider;
import de.chojo.sadu.queries.stages.results.writing.ManipulationBatchQuery;
import de.chojo.sadu.queries.stages.results.writing.ManipulationQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CalledBatchQuery implements QueryProvider {
    private final ParsedQuery parsedQuery;
    private final BatchCall calls;

    public CalledBatchQuery(ParsedQuery parsedQuery, BatchCall calls) {
        this.parsedQuery = parsedQuery;
        this.calls = calls;
    }

    public ManipulationBatchQuery insert() {
        return update();
    }

    public ManipulationBatchQuery update() {
        return query().callConnection(() -> new ManipulationBatchQuery(this, Collections.emptyList()), conn -> {
            var changed = new ArrayList<ManipulationQuery>();
            for (Call call : calls.calls()) {
                try (var stmt = conn.prepareStatement(parsedQuery.sql().tokenizedSql())) {
                    //TODO find way to return generated keys
                    call.apply(parsedQuery.sql(), stmt);
                    changed.add(new ManipulationQuery(this, stmt.executeUpdate()));
                } catch (SQLException ex) {
                    query().handleException(ex);
                }
            }
            return new ManipulationBatchQuery(this, changed);
        });
    }

    public ManipulationBatchQuery delete() {
        return update();
    }

    @Override
    public Query query() {
        return parsedQuery.query();
    }
}
