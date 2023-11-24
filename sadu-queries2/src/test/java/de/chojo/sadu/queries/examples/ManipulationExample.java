package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.call.adapter.Adapter;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.mapped.ManipulationQuery;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class ManipulationExample {
    @Test
    public void example() {
        // Check whether something changed
        boolean change = Query.query("INSERT INTO table VALUES(?)")
                .parameter(Calls.batch(
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID())),
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID()))))
                .insert()
                .changed();

        // Check how many rows changed in total
        int totalRows = Query.query("INSERT INTO table VALUES(?)")
                .parameter(Calls.batch(
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID())),
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID()))))
                .insert()
                .totalRows();

        // Check how many rows for each batch execution were changed
        List<ManipulationQuery> results = Query.query("INSERT INTO table VALUES(?)")
                .parameter(Calls.batch(
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID())),
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID()))))
                .insert()
                .results();
    }
}
