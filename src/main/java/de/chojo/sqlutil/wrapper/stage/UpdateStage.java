package de.chojo.sqlutil.wrapper.stage;

import de.chojo.sqlutil.wrapper.QueryBuilder;
import de.chojo.sqlutil.wrapper.QueryBuilderConfig;
import de.chojo.sqlutil.wrapper.exception.WrappedQueryExecutionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Represents a UpdateStage of a {@link QueryBuilder}.
 * <p>
 * A UpdateStage is used to execute an update and get the changed rows.
 */
public interface UpdateStage {
    /**
     * Execute the update async.
     *
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    CompletableFuture<Integer> execute();

    /**
     * Execute the update async.
     *
     * @param executor executor used for async call
     * @return A {@link CompletableFuture} which returns the number of changed rows.
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    CompletableFuture<Integer> execute(Executor executor);

    /**
     * Execute the update synced.
     *
     * @return Number of changed rows
     * @throws WrappedQueryExecutionException if {@link QueryBuilderConfig#isThrowing()} is set to {@code true} and a exceptions occurs during query building or execution
     */
    int executeSync();
}
