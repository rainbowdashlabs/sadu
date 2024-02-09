/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.reading;

import de.chojo.sadu.queries.stages.execution.reading.MappedQuery;

import java.util.Optional;

/**
 * Base of a result returned by a {@link MappedQuery}
 *
 * @param <T> type of returned object
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Result<T> {
    /**
     * Get the result of the query
     *
     * @return result
     */
    T result();

    default Optional<T> ifPresent() {
        return Optional.ofNullable(result());
    }
}
