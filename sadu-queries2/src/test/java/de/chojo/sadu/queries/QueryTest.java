/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.call.adapter.Adapter;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class QueryTest {

    @Test
    public void syntaxTest() {
        new Query<>()
                .query("INSERT INTO table VALUES(:id, ?, :text)")
                .parameter(Call.of().bind(":id", 1).bind("some input").bind(":text", "amazing text"));

        new Query<>()
                .query("INSERT INTO table VALUES(:uuid)")
                .parameter(Call.of().bind("uuid", Adapter.asBytes(UUID.randomUUID())));
    }

}
