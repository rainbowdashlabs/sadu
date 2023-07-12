/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.exceptions;

/**
 * Represents a function which can throw an exception.
 * <p>
 * this is useful when you need to delegate checked exceptions
 *
 * @param <R> type of result
 * @param <T> type of input
 * @param <E> type of exception
 */
@FunctionalInterface
public interface ThrowingFunction<R, T, E extends Exception> {
    /**
     * A function which maps a value to another value and can throw an exception
     *
     * @param t input
     * @return value
     * @throws E if something went wrong
     */
    R apply(T t) throws E;
}
