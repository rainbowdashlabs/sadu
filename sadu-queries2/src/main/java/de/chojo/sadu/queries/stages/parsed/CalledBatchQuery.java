/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.parsed;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.stages.ParsedQuery;
import de.chojo.sadu.queries.stages.mapped.ManipulationBatchQuery;
import de.chojo.sadu.queries.stages.mapped.ManipulationQuery;

import java.sql.SQLException;
import java.util.ArrayList;

public class CalledBatchQuery {
    private final ParsedQuery<?> parsedQuery;
    private final BatchCall calls;

    public CalledBatchQuery(ParsedQuery<?> parsedQuery, BatchCall calls) {
        this.parsedQuery = parsedQuery;
        this.calls = calls;
    }

    public ManipulationBatchQuery insert() {
        return update();
    }

    public ManipulationBatchQuery update() {
        var changed = new ArrayList<ManipulationQuery>();
        for (Call call : calls.calls()) {
            try (var stmt = parsedQuery.connection().prepareStatement(parsedQuery.sql().tokenizedSql())) {
                //TODO find way to return generated keys
                call.apply(parsedQuery.sql(), stmt);
                changed.add(new ManipulationQuery(stmt.executeUpdate()));
            } catch (SQLException ex) {
                // TODO: logging
            }
        }
        return new ManipulationBatchQuery(changed);
    }

    public ManipulationBatchQuery delete() {
        return update();
    }
}
