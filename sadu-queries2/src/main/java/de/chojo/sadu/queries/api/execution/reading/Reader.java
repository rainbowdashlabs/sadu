/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.execution.reading;

import de.chojo.sadu.queries.api.AppendedQuery;
import de.chojo.sadu.queries.api.results.reading.Result;

import java.util.List;
import java.util.Optional;

/**
 * Interface for retrieving data from a query.
 *
 * @param <V> the type of the data to be retrieved
 */
public interface Reader<V> {
    /**
     * Retrieves a single result from the query.
     *
     * @return the result of the query
     */
    Result<V> one();

    /**
     * Retrieves all the results of the query.
     *
     * @return a Result object containing a List of results
     */
    Result<List<V>> all();

    /**
     * Stores a query result and allows to create a new query.
     *
     * @param key the key used to identify the stored result
     * @return an AppendedQuery object that can execute another query with the same chain
     */
    AppendedQuery storeOneAndAppend(String key);

    /**
     * Stores all query results and allows to create a new query.
     *
     * @param key the key used to identify the stored result
     * @return an AppendedQuery object that can execute another query with the same chain
     */
    AppendedQuery storeAllAndAppend(String key);

    /**
     * Retrieves the first value from the query result and wraps it in an {@link Optional} object.
     * <p>
     * If the query returns no results, an empty {@link Optional} object is returned.
     *
     * @return an {@link Optional} containing the first value from the query result,
     * or an empty {@link Optional} if the query returns no results
     */
    Optional<V> oneAndGet();

    /**
     * Retrieves all elements from the Reader.
     *
     * @return a List of all elements
     */
    List<V> allAndGet();
}
