/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples;

import de.chojo.sadu.PostgresDatabase;
import de.chojo.sadu.postgresql.mapper.PostgresqlMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.results.writing.ManipulationBatchResult;
import de.chojo.sadu.queries.api.results.writing.ManipulationResult;
import de.chojo.sadu.queries.call.adapter.UUIDAdapter;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.configuration.QueryConfigurationBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Stream;

import static de.chojo.sadu.PostgresDatabase.createContainer;
import static de.chojo.sadu.queries.call.adapter.UUIDAdapter.AS_STRING;

@SuppressWarnings({"unused", "RedundantExplicitVariableType"})
public class WriteTest {
    private PostgresDatabase.Database db;
    private QueryConfiguration query;

    @BeforeEach
    void beforeEach() throws IOException, SQLException {
        db = createContainer("postgres", "postgres");
        query = new QueryConfigurationBuilder(db.dataSource()).setRowMapperRegistry(new RowMapperRegistry().register(PostgresqlMapper.getDefaultMapper())).build();
    }

    @AfterEach
    void afterAll() {
        db.close();
    }

    @Test
    public void exampleIndex() {
        // Insert multiple entries at the same time
        ManipulationBatchResult change = query
                // Define the query
                .query("INSERT INTO users(uuid, name) VALUES(?::uuid,?)")
                // Create a new batch call
                .batch(
                        // Define the first call
                        Call.of().bind(UUID.randomUUID(), AS_STRING).bind((String) null),
                        // Define the second call
                        Call.of().bind(UUID.randomUUID(), AS_STRING).bind((String) null))
                // Insert the data
                .insert();

        // Check that something changed
        Assertions.assertTrue(change.changed());
        // Check that two rows were added
        Assertions.assertEquals(2, change.rows());

        // Check how many rows for each batch execution were changed
        for (ManipulationResult result : change.results()) {
            Assertions.assertEquals(1, result.rows());
        }
    }

    @Test
    public void exampleTokenized() {
        // Insert multiple entries at the same time
        ManipulationBatchResult change = query
                // Define the query
                .query("INSERT INTO users(uuid, name) VALUES(:uuid::uuid,?)")
                // Create a new batch call
                .batch(
                        // Define the first call
                        Call.of().bind("uuid", UUID.randomUUID(), AS_STRING).bind((String) null),
                        // Define the second call
                        Call.of().bind("uuid", UUID.randomUUID(), AS_STRING).bind((String) null))
                // Insert the data
                .insert();

        // Check that something changed
        Assertions.assertTrue(change.changed());
        // Check that two rows were added
        Assertions.assertEquals(2, change.rows());

        // Check how many rows for each batch execution were changed
        for (ManipulationResult result : change.results()) {
            Assertions.assertEquals(1, result.rows());
        }
    }

    @Test
    public void exampleStream() {
        // Insert multiple entries at the same time
        ManipulationBatchResult change = query
                // Define the query
                .query("INSERT INTO users(uuid, name) VALUES(:uuid::uuid,?)")
                // Create a new batch call
                .batch(Stream.generate(UUID::randomUUID).limit(2).map(id -> Call.of().bind("uuid", id, AS_STRING).bind((String) null)))
                // Insert the data
                .insert();

        // Check that something changed
        Assertions.assertTrue(change.changed());
        // Check that two rows were added
        Assertions.assertEquals(change.rows(), 2);

        // Check how many rows for each batch execution were changed
        for (ManipulationResult result : change.results()) {
            Assertions.assertEquals(result.rows(), 1);
        }
    }

    @Test
    public void exampleSingle() {
        // Insert multiple entries at the same time
        ManipulationResult change = query
                // Define the query
                .query("INSERT INTO users(uuid, name) VALUES(:uuid::uuid,?)")
                // Create a new call
                .single(Call.of().bind("uuid", UUID.randomUUID(), UUIDAdapter.AS_STRING).bind("someone"))
                // Insert the data
                .insert();

        // Check that something changed
        Assertions.assertTrue(change.changed());
        // Check that two rows were added
        Assertions.assertEquals(change.rows(), 1);
    }
}
