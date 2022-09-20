/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.wrapper.QueryBuilder;

import javax.annotation.CheckReturnValue;

/**
 * Query stage of a {@link QueryBuilder}
 *
 * @param <T> type
 */
public interface QueryStage<T> {
    /**
     * Set the query to execute.
     *
     * @param sql query to set.
     * @return The {@link QueryBuilder} in a {@link StatementStage} with the query defined.
     */
    @CheckReturnValue
    StatementStage<T> query(String sql);

    /**
     * Set the query to execute.
     *
     * @param sql     query to set.
     * @param objects objects to replace in a {@link String#format(String, Object...)}
     *                <b>Do not use this with user input! This should be only used to insert column or table names at runtime</b>
     * @return The {@link QueryBuilder} in a {@link StatementStage} with the query defined.
     */
    @CheckReturnValue
    default StatementStage<T> query(String sql, Object... objects) {
        return query(String.format(sql, objects));
    }

    /**
     * Set the query to execute.
     * <p>
     * This will also skip the statement stage.
     *
     * @param sql query to set.
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the query defined and no parameter set.
     */
    @CheckReturnValue
    ResultStage<T> queryWithoutParams(String sql);

    /**
     * Set the query to execute.
     * <p>
     * This will also skip the statement stage.
     *
     * @param sql     query to set.
     * @param objects objects to replace in a {@link String#format(String, Object...)}
     *                <b>Do not use this with user input! This should be only used to insert column or table names at runtime</b>
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the query defined and no parameter set.
     */
    @CheckReturnValue
    default ResultStage<T> queryWithoutParams(String sql, Object... objects) {
        return queryWithoutParams(String.format(sql, objects));
    }
}
