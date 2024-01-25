/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.call.adapter.Adapter;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.mapped.ManipulationQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_BYTES;

public class ManipulationExample {
    @Test
    public void example() {
        // Check whether something changed
        boolean change = Query.query("INSERT INTO table VALUES(?)")
                .batch(Calls.batch(
                        Call.of().bind(UUID.randomUUID(), AS_BYTES),
                        Call.of().bind(UUID.randomUUID(), AS_BYTES)))
                .insert()
                .changed();

        // Check how many rows changed in total
        int totalRows = Query.query("INSERT INTO table VALUES(?)")
                .batch(Calls.batch(
                        Call.of().bind(UUID.randomUUID(), AS_BYTES),
                        Call.of().bind(UUID.randomUUID(),AS_BYTES)))
                .insert()
                .totalRows();

        // Check how many rows for each batch execution were changed
        List<ManipulationQuery> results = Query.query("INSERT INTO table VALUES(?)")
                .batch(Calls.batch(
                        Call.of().bind(UUID.randomUUID(), AS_BYTES),
                        Call.of().bind(UUID.randomUUID(), AS_BYTES)))
                .insert()
                .results();
    }
}
