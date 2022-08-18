/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.exception.WrappedQueryExecutionException;
import de.chojo.sadu.wrapper.util.UpdateResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Represents a UpdateStage of a {@link QueryBuilder}.
 * <p>
 * A UpdateStage is used to execute an update and get the changed rows.
 */
public interface UpdateStage {
    /**
     * Executes the update.
     *
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     */
    UpdateResult sendSync();

    /**
     * Executes the update async.
     *
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     */
    CompletableFuture<UpdateResult> send();

    /**
     * Executes the update async.
     *
     * @param executor executor used for async call
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     */
    CompletableFuture<UpdateResult> send(Executor executor);

    /**
     * Execute the update async.
     *
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     * @deprecated Deprecated in favor of {@link #send()}
     */
    @Deprecated
    CompletableFuture<Integer> execute();

    /**
     * Execute the update async.
     *
     * @param executor executor used for async call
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     * @deprecated Deprecated in favor of {@link #send(Executor)}
     */
    @Deprecated
    CompletableFuture<Integer> execute(Executor executor);

    /**
     * Execute the update synced.
     *
     * @return Number of changed rows
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     * @deprecated Deprecated in favor of {@link #sendSync()}
     */
    @Deprecated
    int executeSync();
}
