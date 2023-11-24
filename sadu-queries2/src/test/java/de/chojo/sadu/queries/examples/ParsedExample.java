package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.call.adapter.Adapter;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.mapped.ManipulationBatchQuery;
import de.chojo.sadu.queries.stages.mapped.MappedQuery;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ParsedExample {
    @Test
    public void example() {
        // Executing a single call by directly creating the call
        MappedQuery<User> parameter = Query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .parameter(Calls.single(c -> c.bind("some input").bind("name", "user")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")));

        // Map the result to some user object
        MappedQuery<User> parameter1 = Query.query("SELECT * FROM table where uuid = :uuid")
                .parameter(Calls.single(c -> c.bind("uuid", Adapter.asBytes(UUID.randomUUID()))))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")));

        // Perform an insert
        ManipulationBatchQuery insert = Query.query("INSERT INTO table VALUES(?)")
                .parameter(Calls.batch(
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID())),
                        Call.of().bind(Adapter.asBytes(UUID.randomUUID()))))
                .insert();
    }

    public class User{
        private final int id;
        private final UUID uuid;
        private final String name;

        public User(int id, UUID uuid, String name) {
            this.id = id;
            this.uuid = uuid;
            this.name = name;
        }
    }
}
