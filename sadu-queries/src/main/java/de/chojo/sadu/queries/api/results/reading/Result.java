/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.reading;

import de.chojo.sadu.queries.api.results.BaseResult;
import de.chojo.sadu.queries.execution.reading.MappedQuery;

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

    default Optional<T> getIfPresent() {
        return Optional.ofNullable(result());
    }

    default <R> R map(Function<T, R> map) {
        return map.apply(result());
    }
}
