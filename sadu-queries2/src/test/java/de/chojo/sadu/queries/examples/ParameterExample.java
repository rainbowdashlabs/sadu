/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.parsed.CalledBatchQuery;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.stream.Stream;

import static de.chojo.sadu.queries.call.adapter.impl.UUIDAdapter.AS_BYTES;

class ParameterExample {

    @Test
    public void example() {
        // Executing a single call by directly creating the call
        CalledSingletonQuery single1 = Query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .single(Calls.single(Call.of().bind("some input").bind("name", "user")));

        // Creating a single call of the query by using the calls wrapper with a SingletonCall
        CalledSingletonQuery single2 = Query.query("SELECT * FROM table where uuid = :uuid")
                .single(Calls.single(c -> c.bind("uuid", UUID.randomUUID(), AS_BYTES)));

        // Creating multiple calls of the same query by using the batch wrapper
        CalledBatchQuery batch = Query.query("INSERT INTO table VALUES(?)")
                .batch(Calls.batch(
                        Call.of().bind(UUID.randomUUID(), AS_BYTES),
                        Call.of().bind(UUID.randomUUID(), AS_BYTES)));

        BatchCall collect = Stream.generate(UUID::randomUUID)
                .limit(10)
                .map(i -> Call.of().bind(i, AS_BYTES))
                .collect(Calls.collect());
    }

}
