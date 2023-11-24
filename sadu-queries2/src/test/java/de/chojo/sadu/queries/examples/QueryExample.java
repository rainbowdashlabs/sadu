/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.queries.stages.Query;
import org.junit.jupiter.api.Test;

class QueryExample {

    @Test
    public void example() {
        // A query with a named parameter
        Query.query("SELECT * FROM table where uuid = :uuid");

        // A query with a named parameter and one indexed parameter
        Query.query("SELECT * FROM table WHERE id = ?, name ILIKE :name");

        // A query with an indexed parameter
        Query.query("INSERT INTO table VALUES(?)");
    }


}
