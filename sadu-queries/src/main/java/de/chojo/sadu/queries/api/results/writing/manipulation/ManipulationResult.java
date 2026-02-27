/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.results.writing.manipulation;

import de.chojo.sadu.queries.api.results.BaseResult;

import java.util.function.Consumer;

/**
 * The ManipulationResult interface represents the result of a manipulation operation,
 * such as an insert, update, or delete operation.
 */
public interface ManipulationResult extends BaseResult {
    /**
     * Returns the total number of changed rows resulting from a manipulation operation.
     *
     * @return the total number of changed rows
     */
    int rows();

    /**
     * Checks whether at least one row was changed.
     *
     * @return true if one row or more were changed
     */
    boolean changed();

    /**
     * Checks whether the manipulation operation was successful.
     * <p>
     * Success is determined by the absence of any exceptions and at least one row being changed.
     *
     * @return {@code true} if the operation was successful, {@code false} otherwise.
     */
    default boolean isSuccess() {
        return !hasExceptions() && changed();
    }

    /**
     * Executes the given consumer if at least one row was changed.
     *
     * @param consumer the consumer to execute if rows were changed
     * @return result of {@link ManipulationResult#changed()}
     */
    default boolean ifChanged(Consumer<Integer> consumer) {
        if (changed()) {
            consumer.accept(rows());
        }
        return changed();
    }

    /**
     * Executes the given Consumer if the ManipulationResult has no changes.
     *
     * @param notChanged the Consumer to execute
     * @return result of {@link ManipulationResult#changed()}
     */
    default boolean ifEmpty(Consumer<ManipulationResult> notChanged) {
        if (!changed()) {
            notChanged.accept(this);
        }
        return changed();
    }

    /**
     * Executes the provided consumers based on the result of a manipulation operation.
     *
     * @param changed    The consumer to be executed if at least one row was changed. Holds the changed rows count
     * @param notChanged The consumer to be executed if no rows were changed. Holds the result itself.
     * @return result of {@link ManipulationResult#changed()}
     * @see ManipulationResult#rows()
     * @see ManipulationResult#changed()
     */
    default boolean ifChangedOrElse(Consumer<Integer> changed, Consumer<ManipulationResult> notChanged) {
        ifChanged(changed);
        ifEmpty(notChanged);
        return changed();
    }
}
