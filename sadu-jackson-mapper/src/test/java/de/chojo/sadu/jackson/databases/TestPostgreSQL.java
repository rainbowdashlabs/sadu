/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jackson.databases;

import de.chojo.sadu.jackson.TestObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static de.chojo.sadu.jackson.setup.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.api.call.Call.call;
import static de.chojo.sadu.queries.api.query.Query.query;
import static de.chojo.sadu.queries.converter.StandardValueConverter.INSTANT_TIMESTAMP;
import static de.chojo.sadu.queries.converter.StandardValueConverter.UUID_STRING;

public class TestPostgreSQL {
    @BeforeEach
    void setup() throws Exception {
        createContainer();
    }

    public static Stream<TestObj> objects() {
        return Stream.of(
                new TestObj(1324, 78644877545455L, "test", Instant.now(), UUID.randomUUID())
        );
    }

    @ParameterizedTest
    @MethodSource("objects")
    public void testRead(TestObj obj) {
        query("INSERT INTO test(first, second, third, fourth, fifth) VALUES (?,?,?,?,?::UUID)")
                .single(call().bind(obj.first()).bind(obj.second()).bind(obj.third()).bind(obj.fourth(), INSTANT_TIMESTAMP).bind(obj.fifth(), UUID_STRING))
                .insert();

        var result = query("SELECT first, second, third, fourth, fifth::UUID FROM test")
                .single()
                .mapAs(TestObj.class)
                .first()
                .get();

        Assertions.assertEquals(obj, result);
    }
}
