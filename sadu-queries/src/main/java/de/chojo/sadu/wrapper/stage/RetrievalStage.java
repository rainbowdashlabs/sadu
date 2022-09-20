/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import de.chojo.sadu.wrapper.exception.WrappedQueryExecutionException;

import javax.annotation.CheckReturnValue;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Represents a RetrievalStage of a {@link QueryBuilder}.
 * <p>
 * A RetrievalStage is used to retrieve the actual data from the database.
 * <p>
 * The RetrievalStage defines in which way the results should be retrieved and how many.
 *
 * @param <T> type of RetrievalStage
 */
public interface RetrievalStage<T> {

    /**
     * Retrieve all results async as a list
     *
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<List<T>> all();

    /**
     * Retrieve all results async as a list
     *
     * @param executor the executor used for async call
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<List<T>> all(Executor executor);

    /**
     * Retrieve all results synced as a list
     *
     * @return results as list
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    List<T> allSync();

    /**
     * Retrieve the first result from the results set async
     *
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<Optional<T>> first();

    /**
     * Retrieve the first result from the results set async
     *
     * @param executor the executor used for async call
     * @return A {@link CompletableFuture} to retrieve the data.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    CompletableFuture<Optional<T>> first(Executor executor);

    /**
     * Retrieve the first result from the results set synced
     *
     * @return result wrapped into an optional
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    @CheckReturnValue
    Optional<T> firstSync();
}
