/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.exception.WrappedQueryExecutionException;

import javax.annotation.CheckReturnValue;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Stage representing the result of an insert statement.
 */
public interface InsertStage extends UpdateStage {
    /**
     * Retrieve the first created key async
     *
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<Optional<Long>> key();

    /**
     * Retrieve the first created key async
     *
     * @param executor the executor used for async call
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<Optional<Long>> key(Executor executor);

    /**
     * Retrieve the first created key synced
     *
     * @return result wrapped into an optional
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    Optional<Long> keySync();

    /**
     * Retrieve all created keys as a list
     *
     * @return A list of created key.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    List<Long> keysSync();

    /**
     * Retrieve all created keys async as a list
     *
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<List<Long>> keys();

    /**
     * Retrieve all created keys async as a list
     *
     * @param executor the executor used for async call
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and an exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<List<Long>> keys(Executor executor);
}
