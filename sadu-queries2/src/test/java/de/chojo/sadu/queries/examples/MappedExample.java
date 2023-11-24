/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.call.adapter.Adapter;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.stages.Query;
import de.chojo.sadu.queries.stages.results.MultiResult;
import de.chojo.sadu.queries.stages.results.SingleResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MappedExample {

    @Test
    public void example() {
        // Retrieve all matching users directly
        List<User> users = Query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .parameter(Calls.single(c -> c.bind("some input").bind("name", "user")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .allAndGet();

        // Retrieve the first user object directly
        Optional<User> user = Query.query("SELECT * FROM table where uuid = :uuid")
                .parameter(Calls.single(c -> c.bind("uuid", Adapter.asBytes(UUID.randomUUID()))))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .oneAndGet();

        // Retrieve all matching users and store them to use them again later
        // From here on another query could be issued that uses the results of this query
        MultiResult<List<User>> usersResult = Query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name)")
                .parameter(Calls.single(c -> c.bind("some input").bind("name", "user")))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .all();

        // Retrieve the first user and store them it to use it again later
        // From here on another query could be issued that uses the results of this query
        SingleResult<User> userResult = Query.query("SELECT * FROM table where uuid = :uuid")
                .parameter(Calls.single(c -> c.bind("uuid", Adapter.asBytes(UUID.randomUUID()))))
                .map(row -> new User(row.getInt("id"), row.getUuidFromBytes("uuid"), row.getString("name")))
                .one();
    }

    public class User {
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
