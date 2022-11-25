/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.exceptions;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the input to the operation
 * @since 1.8
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws E if something went wrong
     */
    void accept(T t, U u) throws E;
}
