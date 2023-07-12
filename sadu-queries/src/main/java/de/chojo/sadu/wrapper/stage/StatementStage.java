/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.exceptions.ThrowingConsumer;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.util.ParamBuilder;

import javax.annotation.CheckReturnValue;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a StatementStage of a {@link QueryBuilder}.
 * <p>
 * Allows to set query parameter.
 *
 * @param <T> return type
 */
public interface StatementStage<T> {
    /**
     * Set the parameter of the {@link PreparedStatement} of the query.
     *
     * @param stmt statement to change
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the parameters applied to the query.
     * @deprecated This method exists for the sole purpose of backwards compatibility. Usage of {@link #parameter(ThrowingConsumer)} is preferred.
     */
    @Deprecated
    @CheckReturnValue
    ResultStage<T> params(ThrowingConsumer<PreparedStatement, SQLException> stmt);

    /**
     * Set the parameter of the {@link PreparedStatement} of the query.
     * <p>
     * Use the query builder so set the parameters in the defined order.
     * <p>
     * {@code stmt -> stmt.setString("value").setInt(1)}
     *
     * @param params a consumer of a param builder used for simple setting of params.
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the parameters applied to the query.
     * @deprecated use {@link #parameter(ThrowingConsumer)} instead
     */
    @Deprecated(forRemoval = true)
    @CheckReturnValue
    default ResultStage<T> paramsBuilder(ThrowingConsumer<ParamBuilder, SQLException> params) {
        return parameter(params);
    }

    /**
     * Set the parameter of the {@link PreparedStatement} of the query.
     * <p>
     * Use the query builder so set the parameters in the defined order.
     * <p>
     * {@code stmt -> stmt.setString("value").setInt(1)}
     *
     * @param stmt a consumer of a param builder used for simple setting of params.
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the parameters applied to the query.
     */
    @CheckReturnValue
    ResultStage<T> parameter(ThrowingConsumer<ParamBuilder, SQLException> stmt);

    /**
     * Skip this stage and set no parameters in the query.
     * <p>
     * You can also call {@link QueryStage#queryWithoutParams(String)} on the previous {@link QueryStage} instead to avoid this step completely.
     *
     * @return The {@link QueryBuilder} in a {@link ResultStage} with no parameters set.
     */
    @CheckReturnValue
    default ResultStage<T> emptyParams() {
        return params(stmt -> {});
    }
}
