/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.reading;

import de.chojo.sadu.queries.api.results.BaseResult;
import de.chojo.sadu.queries.execution.reading.MappedQuery;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * Base of a result returned by a {@link MappedQuery}
 *
 * @param <T> type of returned object
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Result<T> extends BaseResult {
    /**
     * Get the result of the query
     *
     * @return result
     */
    T result();

    /**
     * Retrieves the result of the query as an optional value.
     *
     * @return an {@code Optional} containing the result of the query, or an empty {@code Optional} if the result is null.
     */
    default Optional<T> get() {
        return Optional.ofNullable(result());
    }

    /**
     * Applies a map function to the result of a query.
     *
     * @param <R> the type of the mapped result
     * @param map the function to apply to the result
     * @return the mapped result
     */
    default <R> R map(Function<T, R> map) {
        return map.apply(result());
    }
}
